package org.egc.commons.command;

import com.google.common.collect.LinkedHashMultimap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
 * @date 2020/12/25
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
     * <p> 输入输出数据文件必须使用全路径
     * <p> 若输出数据只给定了文件名，则使用临时文件目录（"java.io.tmpdir"）作为输出
     *
     * @param exec        the executable
     * @param files       the  files
     * @param inputParams the input params
     * @return {@link ExecResult}
     */
    ExecResult run(@NotBlank String exec, Map<String, String> files, LinkedHashMultimap<String, Object> inputParams);

    /**
     * Run Command.
     * <p> 输入输出数据文件必须使用全路径
     * <p> 若输出数据只给定了文件名，则使用临时文件目录（"java.io.tmpdir"）作为输出
     *
     * @param exec         the executable
     * @param files        the  files
     * @param inputParams  the input params
     * @param envKeyValues the environmental variable list in format: <b>key=value</b>
     * @return {@link ExecResult}
     */
    ExecResult run(@NotBlank String exec, Map<String, String> files, LinkedHashMultimap<String, Object> inputParams, List<String> envKeyValues);

    /**
     * 执行命令
     * <p>可以为输出数据指定存放目录</p>
     * <p> <b>若 Input_* 和 Output_* 文件参数已包含路径， *Dir 参数可为 null</b></p>
     *
     * @param exec        可执行文件，如 PitRemove.若所在目录不在环境变量PATH中，需为全路径
     * @param inputFiles  输入文件参数（含后缀）, Map的值指向一个文件，如 &lt; "-z", Input_Elevation_Grid &gt;， Input_Elevation_Grid 为文件
     * @param outputFiles 输出文件参数（含后缀），如 &lt; "-z", Input_Elevation_Grid &gt;， Input_Elevation_Grid 为文件.
     *                    若 map 的 value 为空（""或null），则使用第一个输入文件名结合 key 值为文件名
     * @param params      非文件类型参数及命令行标记（flag），Map的值为布尔值或数值等.
     *                    如 &lt; "-4way", Fill_Considering_only_4_way_neighbors &gt; Fill_Considering_only_4_way_neighbors 为布尔值.
     *                    相同的键对应的值会合并
     * @param outputDir   工作目录，所有输出文件默认存放目录
     * @return {@link ExecResult}
     */
    ExecResult run(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles,
                   LinkedHashMultimap<String, Object> params, String outputDir);

    /**
     * 执行命令
     * <p>可以为输出数据指定存放目录</p>
     * <p> <b>若 Input_* 和 Output_* 文件参数已包含路径， *Dir 参数可为 null</b></p>
     *
     * @param exec         可执行文件，如 PitRemove.若所在目录不在环境变量PATH中，需为全路径
     * @param inputFiles   输入文件参数（含后缀）, Map的值指向一个文件，如 &lt; "-z", Input_Elevation_Grid &gt;， Input_Elevation_Grid 为文件
     * @param outputFiles  输出文件参数（含后缀），如 &lt; "-z", Input_Elevation_Grid &gt;， Input_Elevation_Grid 为文件.
     *                     若 map 的 value 为空（""或null），则使用第一个输入文件名结合 key 值为文件名
     * @param params       非文件类型参数及命令行标记（flag），Map的值为布尔值或数值等.
     *                     如 &lt; "-4way", Fill_Considering_only_4_way_neighbors &gt; Fill_Considering_only_4_way_neighbors 为布尔值.
     *                     相同的键对应的值会合并
     * @param outputDir    工作目录，所有输出文件默认存放目录
     * @param envKeyValues the environmental variable list in format: <b>key=value</b>
     * @return {@link ExecResult}
     */
    ExecResult run(@NotBlank String exec, Map<String, String> inputFiles, Map<String, String> outputFiles,
                   LinkedHashMultimap<String, Object> params, String outputDir, List<String> envKeyValues);

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
            // 已包含完整路径
            if (StringUtils.isNotBlank(FilenameUtils.getFullPath(file))) {
                return file;
            }
            return FilenameUtils.normalize(workspace + File.separator + file);
        }

        /**
         * 执行命令行
         *
         * @param cmd   the cmd
         * @param files Substitution Map
         * @return boolean boolean
         */
        public static ExecResult execute(CommandLine cmd, Map files) {
            ExecResult result = null;
            if (files != null) {
                cmd.setSubstitutionMap(files);
            }
            try {
                result = CommonsExec.execWithOutput(cmd);
                log.debug(result.getOut());
                // error 中输出的内容类似 This run may take on the order of 1 minutes to complete，并非错误信息
                log.debug(result.getError());
                result.setSuccess(true);
            } catch (IOException io) {
                log.error(io.getMessage(), io);
                result.setSuccess(false);
            }
            return result;
        }

        /**
         * get available processors .
         *
         * @return the int
         */
        public static int processors() {
            int n = Runtime.getRuntime().availableProcessors();
            return n > 2 ? n - 1 : n;
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
        public static void addFileParams(CommandLine cmd, Map<String, String> files, Map<String, String> fileParams,
                                         String dir) {
            if (fileParams == null) {
                return;
            }
            fileParams.forEach((k, v) -> {
                if (StringUtils.isNotBlank(v)) {
                    if (StringUtils.isNotBlank(k)) {
                        cmd.addArgument(k);
                        if (k.startsWith("-")) {
                            k = k.replaceFirst("[-]{1,3}", "");
                        }
                        if (k.endsWith("=")) {
                            k = k.replaceFirst("=", "");
                        }
                        String file = "${" + k + "file}";
                        cmd.addArgument(file);
                        if (StringUtils.isNotBlank(dir)) {
                            files.put(k + "file", RunUtils.filePath(dir, v));
                        } else {
                            files.put(k + "file", v);
                        }
                    }
                    // e.g. Threshold <basefilename>
                    else {
                        cmd.addArgument(v);
                    }
                }
                // if v is blank, do not add the flag to command line
            });
        }

        /**
         * 向命令行中添加非文件类型参数
         * <p>原命令中无需赋值的 flag，通过 Boolean 值来决定改 flag 是否加入命令行。如 "-v" (verbose)
         *
         * @param cmd    {@link CommandLine}
         * @param params 参数 {@link java.util.Map}，如 "-z demfile"
         */
        public static void addParams(CommandLine cmd, LinkedHashMultimap<String, Object> params) {
            if (params == null) {
                return;
            }
            for (Map.Entry entry : params.entries()) {
                String k = (String) entry.getKey();
                Object v = entry.getValue();
                if (k == null || v == null || StringUtils.isBlank(k) || StringUtils.isBlank(String.valueOf(v))) {
                    continue;
                }
                Class<?> vClass = v.getClass();
                if (v instanceof Boolean) {
                    if ((Boolean) v) {
                        cmd.addArgument(k);
                    }
                }
                else if (v instanceof List) {
                    cmd.addArgument(k);
                    List<String> l = new ArrayList<>();
                    for (Object o : (List<?>) v) {
                        if (ClassUtils.isPrimitiveOrWrapper(o.getClass())) {
                            l.add((String) o);
                        } else {
                            //非基本数据类型及其包装类
                            String errorMsg = "Type " + o.getClass().getName() + " of value " + o + " is neither a primitive nor wrapper class";
                            log.error(errorMsg);
                            throw new RuntimeException(errorMsg);
                        }
                    }
                    cmd.addArgument(String.join(" ", l));
                } else if (ClassUtils.isPrimitiveOrWrapper(vClass)) {
                    cmd.addArgument(k);
                    cmd.addArgument(String.valueOf(v));
                } else if (vClass == String.class) {
                    //String 属于引用数据类型 (class, interface, array)
                    cmd.addArgument(k);
                    cmd.addArgument((String) v);
                } else {
                    String errorMsg = "Type " + vClass.getName() + " of value " + v + " is neither a primitive nor wrapper class nor String";
                    log.error(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
            }
        }
    }
}
