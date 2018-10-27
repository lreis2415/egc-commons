package org.egc.commons.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Description:
 * <pre>
 * interface to run command
 * 建议采用<b>单接口适配器模式</b>实现本接口，即
 * 若不需要实现接口中所有方法，则使用一个抽象类（abstract class）来实现（implements）本接口，
 * 并为不需要实现的方法提供默认（空的）实现，然后继承（extends）该抽象类，实现需要的方法
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/25 19:05
 */
public interface RunCommand {

    /**
     * 初始化命令行。
     * <p>当有多个方法需要执行同一程序时可实现本方法(如 TauDEM 中的 mpiexec )。
     * <p>否则返回 null，并在 run 方法中指定可执行程序（exec）即可
     *
     * @return the command line
     */
    CommandLine initCmd();

    /**
     * Run Command.
     * <p>通用，文件使用全路径
     * <p>需要使用命令行标记(flag)时，params 中设置 &lt;flag, true&gt;
     *
     * @param exec   the executable 可执行文件，若所在目录不在环境变量PATH中，需为全路径
     * @param params the params 若参数值为文件，需为全路径
     * @return HashMap with key: {@link CommonsExec#OUT}, {@link CommonsExec#ERROR}, {@link CommonsExec#EXIT_VALUE}
     */
    Map run(@NotBlank String exec, Map<String, Object> params);

    /**
     * Run Command.
     * <p> 输入输出数据文件必须使用全路径
     * <p> 若输出数据只给定了文件名，则使用默认目录（当前目录“.”）作为输出
     *
     * @param exec        the executable
     * @param files       the  files
     * @param inputParams the input params
     * @return HashMap with key: {@link CommonsExec#OUT}, {@link CommonsExec#ERROR}, {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    Map run(@NotBlank String exec, Map<String, String> files, Map<String, Object> inputParams) throws IOException;

    /**
     * Run Command.
     * <p>可以为输入、输出数据指定存放目录</p>
     * <p> <b>若 Input_* 和 Output_* 文件参数已包含路径， *Dir 参数可为 null</b></p>
     *
     * @param exec        the executable 可执行文件，若所在目录不在环境变量PATH中，需为全路径
     * @param inputFiles  the input files 输入文件（含后缀）
     * @param outputFiles the output files 输出文件（含后缀）。若 map 的 value 为空（""或null），则使用第一个输入文件名结合 key 值为文件名
     * @param params      the params 非文件类型参数及命令行标记（flag）
     * @param inputDir    the input dir 输入文件目录
     * @param outputDir   the output dir 输出文件目录
     * @return HashMap with key: {@link CommonsExec#OUT}, {@link CommonsExec#ERROR}, {@link CommonsExec#EXIT_VALUE}
     * @throws IOException the io exception
     */
    Map run(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles, Map<String, Object> params, String inputDir, String outputDir) throws IOException;

    /**
     * 一些所有接口实现类都可使用的方法
     */
    @Slf4j
    abstract class RunUtils {

        /**
         * get filename.
         *
         * @param workspace the workspace
         * @param file      the file
         * @return the string
         */
        public static String filePath(String workspace, @NotNull String file) {
            return FilenameUtils.normalize(workspace + File.separator + file);
        }

        /**
         * 执行命令行
         *
         * @param cmd   the cmd
         * @param files Substitution Map
         * @return boolean boolean
         */
        public static boolean execute(CommandLine cmd, Map files) {
            if (files != null) {
                cmd.setSubstitutionMap(files);
            }
            try {
                Map map = CommonsExec.execWithOutput(cmd);
                String out = (String) map.get("out");
                String error = (String) map.get("error");
                log.debug(out);
                // error 中输出的内容类似 This run may take on the order of 1 minutes to complete，并非错误信息
                log.debug(error);
            } catch (IOException io) {
                log.error(io.getMessage(), io);
                return false;
            }
            return true;
        }

        /**
         * get available processors .
         *
         * @return the int
         */
        public static int processors() {
            return Runtime.getRuntime().availableProcessors();
        }

        /**
         * 向命令行中添加命令行标记
         *
         * @param cmd   {@link CommandLine}
         * @param flags 如 “-v”
         */
        public static void addFlags(CommandLine cmd, String[] flags) {
            if (flags != null && flags.length > 0) {
                for (String flag : flags) {
                    cmd.addArgument(flag);
                }
            }
        }

        /**
         * 向命令行中添加文件类型参数
         *
         * @param cmd        {@link CommandLine}
         * @param files      Substitution Map
         * @param fileParams 文件类型参数 {@link java.util.Map}，如 ("-z", Input_Elevation_Grid)，Input_Elevation_Grid 为 Geotiff 文件
         * @param dir        文件参数中所有文件所在目录
         */
        public static void addFileParams(CommandLine cmd, Map<String, String> files, Map<String, String> fileParams, String dir) {
            fileParams.forEach((k, v) -> {
                if (StringUtils.isNotBlank(v)) {
                    cmd.addArgument(k);
                    if (k.startsWith("-")) {
                        k = k.replaceFirst("-", "");
                    }
                    String file = "${" + k + "file}";
                    cmd.addArgument(file);
                    if (StringUtils.isNotBlank(dir)) {
                        files.put(k + "file", RunUtils.filePath(dir, v));
                    } else {
                        files.put(k + "file", v);
                    }
                }
            });
        }

        /**
         * 向命令行中添加非文件类型参数
         *
         * @param cmd    {@link CommandLine}
         * @param params 参数 {@link java.util.Map}，如 ("-lyrno", 2)
         */
        public static void addParams(CommandLine cmd, Map<String, Object> params) {
            if (params == null) {
                return;
            }
            params.forEach((k, v) -> {
                if (v instanceof Boolean && (Boolean) v) {
                    cmd.addArgument(k);
                } else if (v instanceof Integer && (Integer) v > -1) {
                    cmd.addArgument(k);
                    cmd.addArgument(String.valueOf(v));
                } else if (v instanceof Long && (Long) v > -1) {
                    cmd.addArgument(k);
                    cmd.addArgument(String.valueOf(v));
                } else if (v instanceof Double && (Double) v > -1) {
                    cmd.addArgument(k);
                    cmd.addArgument(String.valueOf(v));
                } else if (v instanceof String) {
                    cmd.addArgument(k);
                    cmd.addArgument(String.valueOf(v));
                }
            });
        }
    }
}
