import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.text.SimpleDateFormat;

class PhotoOptimizer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String sourceDir;
        String destinationDir;

        while (true) {
            System.out.print("Введите путь к директории с фотографиями: ");
            sourceDir = scanner.nextLine();

            if (isValidDirectory(sourceDir)) {
                break;
            } else {
                System.err.println("Неверный путь к директории. Попробуйте снова.");
            }
        }

        while (true) {
            System.out.print("Введите путь к директории для сортировки: ");
            destinationDir = scanner.nextLine();

            if (isValidDirectory(destinationDir)) {
                break;
            } else {
                System.err.println("Неверный путь к директории. Попробуйте снова.");
            }
        }

        try {
            PhotoGallery gallery = new PhotoGallery(sourceDir);
            gallery.optimize();
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

        try {
            DateSorting copier = new DateSorting();
            copier.copyPhotos(sourceDir, destinationDir);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static boolean isValidDirectory(String pathFile){
        File dir = new File(pathFile);
        return dir.exists() && dir.isDirectory();
    }
}

class PhotoGallery {
    private final List<Photo> photos;

    public PhotoGallery(String sourceDir) {
        this.photos = findPhotos(sourceDir);
    }

    private List<Photo> findPhotos(String sourceDir) {
        List<Photo> photos = new ArrayList<>();
        File dir = new File(sourceDir);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile() && (file.getName().toLowerCase().endsWith(".jpg") ||
                        file.getName().toLowerCase().endsWith(".jpeg") || file.getName().toLowerCase().endsWith(".png") ||
                        file.getName().toLowerCase().endsWith(".gif"))) {
                    photos.add(new Photo(file));
                }
            }
        }
        return photos;
    }

    public void optimize() throws IOException, NoSuchAlgorithmException {
        removeDuplicates();
    }

    private void removeDuplicates() throws IOException, NoSuchAlgorithmException {
        Map<String, Photo> uniquePhotos = new HashMap<>();
        for (Photo photo : photos) {
            String hash = photo.calculateHash();
            if (!uniquePhotos.containsKey(hash)) {
                uniquePhotos.put(hash, photo);
            } else {
                photo.deleteFile();
                System.out.println("Удален дубликат: " + photo.getFileName());
            }
        }
        System.out.println("Оптимизация завершена.");
    }

}

class Photo {
    private final File file;

    public Photo(File file) { this.file = file;}

    public String getFileName() { return file.getName();}

    public String calculateHash() throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int nread;
            while ((nread = fis.read(buffer)) != -1) {
                md.update(buffer, 0, nread);
            }
            byte[] digest = md.digest();
            return bytesToHex(digest);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public void deleteFile() {
        if (!file.delete()) {
            System.err.println("Ошибка удаления файла: " + file.getName());
        }
    }
}

class DateSorting {
    public void copyPhotos(String sourceDir, String destinationDir2) throws IOException {
        File sourceDirectory = new File(sourceDir);
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
            System.err.println("Исходная директория не существует или не является директорией.");
            return;
        }

        List<File> photos = getPhotoFiles(sourceDirectory);
        photos.sort(Comparator.comparingLong(File::lastModified));

        for (File photo : photos) {
            sortDate(photo, destinationDir2);
        }
        System.out.println("Копирование завершено.");
    }

    private List<File> getPhotoFiles(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return new ArrayList<>();
        List<File> photoFiles = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && isImageFile(file)) {
                photoFiles.add(file);
            }
        }
        return photoFiles;
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") ||
                fileName.endsWith(".gif") || fileName.endsWith(".bmp");
    }

    private void sortDate(File photo, String destinationDir2) throws IOException {
        Date creationDate = new Date(photo.lastModified());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
        String dateString = dateFormat.format(creationDate);
        String destPath = destinationDir2 + "/" + dateString + "/" + photo.getName();
        Path source = photo.toPath();
        Path dest = Paths.get(destPath);
        Files.createDirectories(dest.getParent());
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Скопировано: " + photo.getName() + " в " + destPath);
    }
}