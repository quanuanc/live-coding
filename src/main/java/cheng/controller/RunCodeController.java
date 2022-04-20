package cheng.controller;

import cheng.service.ExecuteStringSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RunCodeController {
    private final Logger logger = LoggerFactory.getLogger(RunCodeController.class);

    @Autowired
    private ExecuteStringSourceService executeStringSourceService;

    private static final String DEFAULT_SOURCE = """
            public class Run{
                public static void main(String[] args){
                    System.out.println("Hello World");
                }
            }
            """;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String entry(Model model) {
        model.addAttribute("lastSource", DEFAULT_SOURCE);
        return "ide";
    }

    @RequestMapping(path = "/run", method = RequestMethod.POST)
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
