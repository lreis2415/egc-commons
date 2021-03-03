package org.egc.commons.test;

import org.apache.commons.io.FileUtils;
import org.egc.commons.util.PathUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.apache.commons.lang3.SystemUtils.JAVA_IO_TMPDIR;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2019/9/4 16:06
 */
public class PathTest {
    @Test
    public void pathTest() {
        System.out.println(PathUtil.getPackagePath(this.getClass()));
        System.out.println(PathUtil.getClassPath(this.getClass()));
        System.out.println(PathUtil.getClassPath());
    }

    @Test
    public void pathTest2() {
        System.out.println(PathUtil.classFilePath("/test.properties"));
        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
    }

    @Test
    public void test() throws IOException {
        Path tmpDir = Paths.get(FileUtils.getTempDirectory().getAbsolutePath());
        tmpDir.toFile().deleteOnExit();
        Path path = Files.createTempDirectory(tmpDir, "egc_cmd_");
        System.out.println(path);
    }
}
