package cheng.controller;

import cheng.service.ExecuteStringSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RunCodeController {
    @Autowired
    public RunCodeController(ExecuteStringSourceService executeStringSourceService) {
        this.executeStringSourceService = executeStringSourceService;
    }

    private final ExecuteStringSourceService executeStringSourceService;

    private static final String DEFAULT_SOURCE = """
            public class Run {
                public static void main(String[] args){
                    System.out.println("Hello From live-coding!");
                }
            }
            """;

    @GetMapping("/")
    public String entry(Model model) {
        model.addAttribute("lastSource", DEFAULT_SOURCE);
        return "ide";
    }

    @PostMapping("/run")
    public String runCode(@RequestParam("source") String source,
                          @RequestParam("systemIn") String systemIn,
                          Model model) {
        String runResult = executeStringSourceService.execute(source, systemIn);
        runResult = runResult.replaceAll(System.lineSeparator(), "<br/>");

        model.addAttribute("lastSource", source);
        model.addAttribute("lastSystemIn", systemIn);
        model.addAttribute("runResult", runResult);

        return "ide";
    }
}
