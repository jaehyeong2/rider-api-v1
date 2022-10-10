package jjfactory.simpleapi.business.rider.service;


import jjfactory.simpleapi.business.insurance.domain.HistoryType;
import jjfactory.simpleapi.business.insurance.domain.InsuranceHistory;
import jjfactory.simpleapi.business.insurance.repository.InsuranceHistoryRepository;
import jjfactory.simpleapi.business.rider.domain.Rider;
import jjfactory.simpleapi.business.rider.dto.req.LoginReq;
import jjfactory.simpleapi.business.rider.dto.req.RiderCreate;
import jjfactory.simpleapi.business.rider.dto.res.TokenRes;
import jjfactory.simpleapi.business.rider.repository.RiderRepository;
import jjfactory.simpleapi.business.seller.domain.Seller;
import jjfactory.simpleapi.business.seller.repository.SellerRepository;
import jjfactory.simpleapi.global.config.auth.TokenProvider;
import jjfactory.simpleapi.global.ex.BusinessException;
import jjfactory.simpleapi.global.ex.ErrorCode;
import jjfactory.simpleapi.global.uitls.ImageUtil;
import jjfactory.simpleapi.global.uitls.enc.AES_Encryption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RiderRepository riderRepository;
    private final SellerRepository sellerRepository;
    private final InsuranceHistoryRepository insuranceHistoryRepository;

    public Long signUp(RiderCreate req, MultipartFile InsuranceImage) {
        // 주민등록번호 형태 체크 (13자리인지. 하이픈 없는지)
        ssnCheck(req.getSsn());

        Seller seller = sellerRepository.findBySellerCode(req.getSellerCode());

        String encPassword = passwordEncoder.encode(req.getPassword());

        Rider rider = Rider.create(req, seller,encPassword);
        riderRepository.save(rider);

        InsuranceHistory insuranceHistory = InsuranceHistory.create(rider, HistoryType.REQUEST, 2);
        insuranceHistoryRepository.save(insuranceHistory);

        if(InsuranceImage != null){
            String folderPath = rider.getId() + "/";
            String filePath = folderPath + InsuranceImage.getOriginalFilename();

            try{
                ImageUtil.saveFile(InsuranceImage,filePath,folderPath);
                rider.updateImagePath(filePath);
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }
        return rider.getId();
    }

    @Transactional(readOnly = true)
    public TokenRes login(LoginReq dto){
        Rider rider = riderRepository.findByLoginId(dto.getLoginId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NOT_FOUND_USER);
        });

        matchPassword(dto.getPassword(),rider.getPassword());
        String token = createToken(rider);

        return new TokenRes(token);
    }

    //jwt 토큰생성
    public String createToken(Rider rider){
        return tokenProvider.createToken(rider.getLoginId(),rider.getRoles());
    }

    public void matchPassword(String reqPassword, String userPassword){
        boolean matches = passwordEncoder.matches(reqPassword, userPassword);

        if(!matches){
            throw new BusinessException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    public void ssnCheck(String encSsn){
        String rawSsn = "";
        try {
            rawSsn = aesDecode(encSsn);
        } catch (Exception e) {
            log.error("{}",e);
            throw new BusinessException(ErrorCode.INVALID_ENCODE_TYPE);
        }
        hyphenCheck(rawSsn);
        ssnLengthCheck(rawSsn);
    }

    private void hyphenCheck(String number) {
        if (number.contains("-") || (!number.matches("[+-]?\\d*(\\.\\d+)?"))){
            throw new BusinessException(ErrorCode.INVALID_TYPE_VALUE2);
        }
    }

    private void ssnLengthCheck(String number) {
        if (number.length() != 13) {
            throw new BusinessException(ErrorCode.INVALID_LENGTH_VALUE);
        }
    }

    private String aesEncode(String str) throws Exception {
        AES_Encryption aes = new AES_Encryption();
        String encrypt = aes.encrypt(str);
        return encrypt;
    }

    private String aesDecode(String str) throws Exception {
        AES_Encryption aes = new AES_Encryption();
        String decrypt = aes.decrypt(str);
        return decrypt;
    }
}
