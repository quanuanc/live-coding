package cheng.controller;

import cheng.service.ExecuteStringSourceService;
import cheng.vo.Input;
import cheng.vo.Output;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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
        Output output = new Output();
        output.setInput(input);
        String runResult = executeStringSourceService.execute(input.getSource(), input.getSystemIn());
        output.setRunResult(runResult);
        return output;
    }

}
