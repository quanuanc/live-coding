package cheng.controller;

import cheng.service.ExecuteStringSourceService;
import cheng.vo.Input;
import cheng.vo.Output;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private static final String DEFAULT_SOURCE = """
            public class Run {
                public static void main(String[] args){
                    System.out.println("Hello From live-coding!");
                }
            }
            """;
    private final ExecuteStringSourceService executeStringSourceService;

    @GetMapping
    public Output home() {
        log.info("home");
        Output output = new Output();
        Input input = new Input();
        input.setSource(DEFAULT_SOURCE);
        input.setSystemIn("");
        output.setInput(input);
        output.setRunResult("");
        return output;
    }

    @PostMapping("/run")
    public Output runCode(@RequestBody Input input) {
        log.info("runCode");
        Output output = new Output();
        output.setInput(input);
        String runResult = executeStringSourceService.execute(input.getSource(), input.getSystemIn());
        output.setRunResult(runResult);
        return output;
    }

}
