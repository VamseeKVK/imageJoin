/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageJoin;

/**
 *
 * @author Vamsee Krishna Kakumanu
 *         CSE 3rd year
 *         VNIT_16-20
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Combine {
    static int j;   
   public static void combine() throws FileNotFoundException, DocumentException, IOException, ArrayIndexOutOfBoundsException{
       
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(CombinerWindow.outputPath+"/"+CombinerWindow.outputName));
        document.open();
        for (j=0;j < CombinerWindow.combineList.size();j++) {
            document.newPage();
            Image image = Image.getInstance(CombinerWindow.combineList.get(j));
            image.setAbsolutePosition(0, 0);
            image.setBorderWidth(0);
            image.scaleAbsolute(PageSize.A4);
            document.add(image);
        }
        document.close(); 
        System.out.println("Images have been Saved");
    }   
    
}
