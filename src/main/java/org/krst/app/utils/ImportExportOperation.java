package org.krst.app.utils;

public enum ImportExportOperation {
    IMPORT_STUDENT_TEMPLATE("K2q8D!&s", "批量导入学生信息", "学生信息批量导入模板.xlsx"),
    EXPORT_ALL_STUDENT_INFO("H86D$8#a", "批量导出学生信息", "学生信息批量导出模板.xlsx");

    private final String verificationCode;
    private final String operationName;
    private final String fileName;

    ImportExportOperation(String verificationCode, String operationName, String fileName) {
        this.verificationCode = verificationCode;
        this.operationName = operationName;
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Object getPath() {
        return "/" + this.fileName;
    }

    public String getInternalPath() {
        return "classpath:templates/" + this.fileName;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public boolean verifyFile(String verificationCode) {
        return this.verificationCode.equals(verificationCode);
    }
}
