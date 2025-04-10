package com.proyecto.carnesena.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.proyecto.carnesena.model.estado_user;
import com.proyecto.carnesena.model.usuario;
import com.proyecto.carnesena.service.usuarioService;

@RestController
@RequestMapping("/api/carnet")
public class CarnetPDFController {
    private final usuarioService usuarioService;

    public CarnetPDFController(usuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> generarCarnet(@PathVariable String id) {
        usuario usuario = usuarioService.findOne(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Rectangle carnetSize = new Rectangle(155.91f, 226.77f);
            Document document = new Document(carnetSize);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // Fuentes
            Font fontGreen = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, new BaseColor(57, 181, 74));
            Font fontNormal = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
            Font fontRegional = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
            Font fontCentro = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD, new BaseColor(57, 181, 74));
            Font fontGrupo = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

            // Logo
            InputStream logoStream = getClass().getClassLoader().getResourceAsStream("images/logo.png");
            if (logoStream != null) {
                Image logo = Image.getInstance(logoStream.readAllBytes());
                logo.scaleAbsolute(44, 44);
                logo.setAbsolutePosition(10, 178);
                document.add(logo);
            }

            // Foto del usuario dentro del marco
            if (usuario.getFoto() != null) {
                Image foto = Image.getInstance(usuario.getFoto()); // Asegúrate de que devuelve un byte[]
                foto.scaleAbsolute(60, 87); // Ajusta el tamaño al marco
                foto.setAbsolutePosition(84.5f, 131f); // Ubícala dentro del marco
                document.add(foto);
            }

            PdfContentByte canvas = writer.getDirectContent();
            canvas.setColorStroke(new BaseColor(57, 181, 74));
            canvas.moveTo(10, 127);
            canvas.lineTo(145, 127);
            canvas.stroke();

            // Nombre y Apellido
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(usuario.getNombre(), fontGreen), 10, 112,
                    0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(usuario.getApellidos(), fontGreen), 10,
                    102, 0);

            // Documento y Tipo de Sangre
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(usuario.getTipo_documento()), fontNormal), 10, 88, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(usuario.getNumero_documento()), fontNormal), 29, 88, 0);

            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(usuario.getTipo_sangre(), fontNormal),
                    132, 88, 0);

            // Código de Barras
            Barcode128 barcode = new Barcode128();
            barcode.setCode(String.valueOf(usuario.getNumero_documento()));
            barcode.setCodeType(Barcode128.CODE128);
            barcode.setFont(null);
            Image barcodeImage = barcode.createImageWithBarcode(canvas, BaseColor.BLACK, BaseColor.BLACK);
            barcodeImage.setAbsolutePosition(10, 66);
            barcodeImage.scaleAbsolute(80, 15);
            document.add(barcodeImage);

            float startX = 10; // Más a la izquierda
            float endX = 30; // Más corta
            canvas.moveTo(startX, 57);
            canvas.lineTo(endX, 57);
            canvas.stroke();

            // Información inferior
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Regional Huila", fontRegional), 10, 45,
                    0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                    new Phrase("El centro de la Industria, La empresa", fontCentro), 10, 37, 0);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("y los Servicios", fontCentro), 10, 29,
                    0);

            // Nombre del programa y código de ficha
            String programa = usuario.getFicha().getNombre_programa();
            Font fontPrograma = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
            Phrase phrasePrograma = new Phrase(programa, fontPrograma);
            float textWidth = ColumnText.getWidth(phrasePrograma);

            if (textWidth > 130) {
                String[] palabras = programa.split(" ");
                StringBuilder linea1 = new StringBuilder();
                StringBuilder linea2 = new StringBuilder();
                float widthLinea1 = 0;

                for (String palabra : palabras) {
                    float palabraWidth = ColumnText.getWidth(new Phrase(palabra + " ", fontPrograma));
                    if (widthLinea1 + palabraWidth <= 130) {
                        linea1.append(palabra).append(" ");
                        widthLinea1 += palabraWidth;
                    } else {
                        linea2.append(palabra).append(" ");
                    }
                }
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                        new Phrase(linea1.toString().trim(), fontPrograma), 10, 20, 0);

                if (!linea2.toString().trim().isEmpty()) { // Solo usa la segunda línea si es necesario
                    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                            new Phrase(linea2.toString().trim(), fontPrograma), 10, 12, 0);
                    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                            new Phrase("Grupo No." + usuario.getFicha().getCodigo_ficha(), fontGrupo), 10, 5, 0);
                } else {
                    ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                            new Phrase("Grupo No." + usuario.getFicha().getCodigo_ficha(), fontGrupo), 10, 12, 0);
                }
            } else {
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, phrasePrograma, 10, 15, 0);
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT,
                        new Phrase("Grupo No." + usuario.getFicha().getCodigo_ficha(), fontGrupo), 10, 5, 0);
            }

            document.close();

            usuario.setEstado_user(estado_user.descargado);
            usuarioService.save(usuario);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "carnet.pdf");
            return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}