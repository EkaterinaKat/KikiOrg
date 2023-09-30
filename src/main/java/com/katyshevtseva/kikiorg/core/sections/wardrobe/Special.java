package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.repo.PieceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class Special {
    private static final String SOURCE_DIR_URL = "D:\\onedrive\\central_image_storage\\wardrobe\\";
    private static final String DESTINATION_DIR_URL = "D:\\onedrive\\central_image_storage\\wardrobe_active\\";
    private final PieceRepo pieceRepo;

    @PostConstruct
    private void copyActivePieceImages() {
        File destination = getDestination();
        if (destination == null)
            return;

        for (Piece piece : pieceRepo.findByEndDateIsNull()) {
            File file = new File(SOURCE_DIR_URL, piece.getImageFileName());
            copyFileUsingStream(file, new File(destination, piece.getImageFileName()));
        }
    }

    private static void copyFileUsingStream(File source, File dest) {
        try (InputStream is = Files.newInputStream(source.toPath());
             OutputStream os = Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getDestination() {
        File destination = new File(DESTINATION_DIR_URL);
        if (destination.exists()) {
            if (!destination.delete()) {
                System.out.println("destination deletion failed");
                return null;
            }
        }
        if (!destination.mkdir()) {
            System.out.println("destination creation failed");
            return null;
        }
        return destination;
    }
}
