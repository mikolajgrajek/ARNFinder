package com.mgrajek.arn_finder.output;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;

import com.mgrajek.arn_finder.domain.ARNMatchedResult;

import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class HtmlOutputPrinter {
  public static void printToFile(ARNMatchedResult result, File file) throws IOException {
    String text = new HtmlOutputPrinter().print(result);
    FileUtils.write(file, text, "UTF-8");
  }

  private String print(ARNMatchedResult pluginResult) {
    try {
      final Template template = create().getTemplate("html_template.ftl");
      try (StringWriter mailDetailsWriter = new StringWriter()) {
        template.process(pluginResult, mailDetailsWriter);
        return mailDetailsWriter.toString();
      }
    } catch (Exception e) {
      throw new RuntimeException("Cannot fill in template with pluginRunResult status!", e);
    }
  }

  public static Configuration create() {
    Configuration freeMarkerConfig = new Configuration(new Version(2, 3, 21));
    freeMarkerConfig.setObjectWrapper(new BeansWrapperBuilder(new Version(2, 3, 21)).build());
    freeMarkerConfig.setDefaultEncoding("UTF-8");
    freeMarkerConfig.setTemplateLoader(new ClassTemplateLoader(HtmlOutputPrinter.class, ""));
    freeMarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
    return freeMarkerConfig;
  }
}
