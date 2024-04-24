package model;

public class myFile {
    // Attributes of myFile
    private final String filename;
    private final String fileType;
    private final String fileSize;
    private final byte[] file;

    // Private constructor
    private myFile(String filename, String fileType, String fileSize, byte[] file) {
        this.filename = filename;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.file = file;
    }

    // Getters for the attributes (no setters to maintain immutability)
    public String getFilename() {
        return filename;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public byte[] getFile() {
        return file;
    }

    // Builder interface
    public interface Builder {
        Builder setFilename(String filename);
        Builder setFileType(String fileType);
        Builder setFileSize(String fileSize);
        Builder setFile(byte[] file);
        myFile build();
    }

    // Concrete Builder
    public static class ConcreteBuilder implements Builder {
        private String filename;
        private String fileType;
        private String fileSize;
        private byte[] file;

        @Override
        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        @Override
        public Builder setFileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        @Override
        public Builder setFileSize(String fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        @Override
        public Builder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        @Override
        public myFile build() {
            return new myFile(filename, fileType, fileSize, file);
        }
    }

    // Director
    public static class Director {
        public myFile constructFile(Builder builder, String filename, String fileType, String fileSize, byte[] file) {
            return builder.setFilename(filename)
                    .setFileType(fileType)
                    .setFileSize(fileSize)
                    .setFile(file)
                    .build();
        }
    }
}