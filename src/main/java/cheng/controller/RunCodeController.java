package cheng.controller;

import cheng.service.ExecuteStringSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RunCodeController {
    private static final String DEFAULT_SOURCE = """
            import java.util.Scanner;
            
            public class Run {
                public static void main(String[] args){
                    Scanner sc = new Scanner(System.in);
                    String name = sc.nextLine();
                    System.out.println("Hello from live-coding!");
                    System.out.println("Good morning, " + name + "!");
                }
            }
            """;
    private static final String DEFAULT_SYSTEM_IN = "bro";
    private final ExecuteStringSourceService executeStringSourceService;

    @GetMapping("/")
    public String entry(Model model) {
        model.addAttribute("lastSource", DEFAULT_SOURCE);
        model.addAttribute("lastSystemIn", DEFAULT_SYSTEM_IN);
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
