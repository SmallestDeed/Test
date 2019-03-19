package com.sandu.search.controller.monitor;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * IK分词器扩展
 *
 * @date 2018/5/21
 * @auth pengxuangang
 * @mail xuangangpeng@gmail.com
 */
@Data
@Controller
@RequestMapping("/ikextend")
public class IKExtendController {

    @Value("${fullsearch.version}")
    private String version;
    //扩展字典
    private List<String> dictList;

    @RequestMapping("/dict")
    String dict(HttpServletResponse response) {

        response.setHeader("Last-Modified", "2018-05-31 20:00:00");
        response.setHeader("ETag", version);

        return "/ik-extend-dict.properties";
    }
}
