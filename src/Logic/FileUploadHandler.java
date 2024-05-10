package Logic;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.*;

public class FileUploadHandler {

  private final Path storageDirectory;

  public FileUploadHandler(String directory) {
    this.storageDirectory = Paths.get(directory);
    prepareStorageDirectory();
  }

  private void prepareStorageDirectory() {
    if (!Files.exists(storageDirectory)) {
      try {
        Files.createDirectories(storageDirectory);
      } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to create storage directory: " + e.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  public JFileChooser createImageFileChooser() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select an Image File");
    fileChooser.setAcceptAllFileFilterUsed(false);
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
    fileChooser.addChoosableFileFilter(filter);
    return fileChooser;
  }

  public String saveFileToDisk(int userId, Path sourcePath) throws IOException {
    String extension = getFileExtension(sourcePath);
    int userPostNumber = getNextUserPostNumber(userId, extension);
    String newFileName = userId + "_" + userPostNumber + "." + extension;
    Path destinationPath = storageDirectory.resolve(newFileName);
    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
    return destinationPath.toString();
  }

  private String getFileExtension(Path path) {
    String fileName = path.toString();
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex != -1) {
      return fileName.substring(dotIndex + 1);
    }
    return "";
  }

  private int getNextUserPostNumber(int userId, String extension) {
    int maxNum = 0;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(storageDirectory, userId + "_*." + extension)) {
      for (Path entry : stream) {
        String fileName = entry.getFileName().toString();
        int underscoreIndex = fileName.indexOf('_');
        int dotIndex = fileName.lastIndexOf('.');
        if (underscoreIndex > 0 && dotIndex > underscoreIndex) {
          String numStr = fileName.substring(underscoreIndex + 1, dotIndex);
          try {
            int num = Integer.parseInt(numStr);
            if (num > maxNum) {
              maxNum = num;
            }
          } catch (NumberFormatException e) {
            // Handle if necessary
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return maxNum + 1;
  }
}
