package org.krst.app.services.supports;

import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.krst.app.configurations.Logger;
import org.krst.app.utils.CommonUtils;
import org.krst.app.utils.ImportExportOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;

@Service
public class TemplateFileSupport {
    @Autowired private Logger logger;
    @Autowired private ExcelSupport excelSupport;

    public File chooseDirectory(Window ownerWindow) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(ownerWindow);
    }

    public File chooseFile(Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(ownerWindow);
    }

    public void generateTemplateFile(Window ownerWindow, ImportExportOperation templateFile) {
        File src;
        try {
            src = ResourceUtils.getFile(templateFile.getInternalPath());
        } catch (Exception e) {
            logger.logError(this.getClass().toString(), "Failed to get template file: {}", templateFile.getFileName());
            CommonUtils.alertSystemError("无法获取模板文件: " + e.getMessage());
            return;
        }

        File dst;
        try {
            dst = new File(chooseDirectory(ownerWindow).getPath() + templateFile.getPath());
        } catch (NullPointerException e) {
            return;
        } catch (Exception e) {
            logger.logError(this.getClass().toString(), "Failed to get the destination file: {}", templateFile.getFileName());
            CommonUtils.alertSystemError("无法在目标目录创建新文件: " + e.getMessage());
            return;
        }

        try {
            FileCopyUtils.copy(src, dst);
        } catch (Exception e) {
            logger.logError(this.getClass().toString(), "Failed to copy file: src={}, dst={}, e-{}", src.getPath(), dst.getName(), e.getMessage());
            CommonUtils.alertSystemError("执行复制模板文件错误: " + e.getMessage());
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("模板文件生成成功");
        alert.setHeaderText("模板文件生成成功");
        alert.setContentText("模板文件 [" + templateFile.getFileName() + "] 已在您所选定的目录生成成功！" +
                "\n\n请您使用此文件填充信息，但请勿更改表头信息；您可更改列宽行宽。");
        alert.show();
    }

    public boolean verifyTemplateFile(File file, ImportExportOperation importExportOperation) {
        List<String> verificationCodes;
        try {
            verificationCodes = excelSupport.getMetaData(file, "校验码");
        } catch (Exception e) {
            return false;
        }
        return importExportOperation.verifyFile(verificationCodes.get(0));
    }

    public File getNewFile(File directory, ImportExportOperation operation) {
        File dstFile = new File(directory.getPath() + operation.getPath());
        try {
            if (dstFile.createNewFile()) return dstFile;
        } catch (Exception e) {
            CommonUtils.alertSystemError("无法创建文件：" + operation.getFileName() +
                    "\n请查看是否有同名文件存在，或重新选择路径！\n" +
                    "错误原因：" + e.getMessage());
        }

        return null;
    }
}
