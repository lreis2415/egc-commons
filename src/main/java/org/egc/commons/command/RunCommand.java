package org.egc.commons.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RunCommand {
  private static final Logger log = LoggerFactory.getLogger(RunCommand.class);
  public int runCommond(String commondLine){
    final CommandLine cmdLine = CommandLine.parse(commondLine);
    DefaultExecutor executor = new DefaultExecutor();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    executor.setStreamHandler(new PumpStreamHandler(baos, baos));
    int exitValue = -9999;
    try {
      exitValue = executor.execute(cmdLine);
    }catch (IOException e){
      log.error(e.toString());
    }
    final String result = baos.toString().trim();
    System.out.println(result);
    return exitValue;
    }

}
