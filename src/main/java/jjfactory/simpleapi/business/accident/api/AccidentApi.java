package jjfactory.simpleapi.business.accident.api;

import jjfactory.simpleapi.business.accident.service.AccidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/accidents")
@RequiredArgsConstructor
@RestController
public class AccidentApi {
    private final AccidentService accidentService;
}
