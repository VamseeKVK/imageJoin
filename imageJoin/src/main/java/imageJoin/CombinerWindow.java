/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageJoin;

import com.itextpdf.text.DocumentException;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

/**
 *
 * @author Vamsee Krishna Kakumanu
 *         CSE 3rd Year
 *         VNIT_16-20
 */
public class CombinerWindow extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -13741701175021492L;

	/**
     * Creates new form CombinerWindow
     */
    public CombinerWindow() {
        initComponents();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(15);
        jScrollPane1.getHorizontalScrollBar().setUnitIncrement(15);
        jTextArea_Alert.setText("If you want to Resume your work press Resume,If you press anyother button, Saved progress in last session will be lost.");
    }
    
    
     int pos = 0;
    static ArrayList<String> combineList = new ArrayList<>();
    String[] imagesList;
    static String imageName,outputName,outputPath,lastVisited_Path;
    static boolean status;
        
    /**
     * Gets the Array of filenames in the given directory
     * @param path
     * @return 
     */
    
    public String[] getImages(String path){
        File file = new File(path);
        String[] imageList;
        imageList = file.list();
        Arrays.sort(imageList);
        return imageList;           
    }
    
    /**
     * Displays the image in the preview pane 
     * @param index
     */
    
    public final void showImage(int index){
        try{
            imagesList = getImages(textField_Path); 
            imageName = imagesList[index];
            ImageIcon icon = new ImageIcon(textField_Path+"/"+imageName);
            Image image = icon.getImage().getScaledInstance(jLabel_Image.getWidth(), jLabel_Image.getHeight(), Image.SCALE_SMOOTH);
            jLabel_Image.setIcon(new ImageIcon(image));
            jTextField_ImageName.setText(imageName);
        }
        catch(NullPointerException e){
            jTextArea_Alert.setText("Open folder with Images.");
        }
    }
      
    public void go(){
        try{
            textField_Path = jTextField_Path.getText();
            pos=0;
            showImage(pos);
            combineList.clear();
            jTextField_Class.setText("");
            jTextField_Subject.setText("");
            jTextField_StudentID.setText("");
            clearsave();
        }
        catch(NullPointerException e){
            jTextArea_Alert.setText("Press Open And select a folder OR Enter Path to folder manually.");
        }
    }
        
    public void next(){
        try{
            pos = pos + 1;
            if(pos >= getImages(textField_Path).length)
            {
                pos = getImages(textField_Path).length - 1;
                jTextArea_Alert.setText("Last Image in this Folder.");
            }
            showImage(pos);
        }
        catch(NullPointerException e){
            jTextArea_Alert.setText("Go to a folder to view Images.");
        }
    }
    
    public void back(){
        try{    
            pos = pos - 1;
            if(pos < 0)
            {
                pos = 0;
                jTextArea_Alert.setText("First Image in this Folder.");
            }
            showImage(pos);
        }
        catch(NullPointerException e){
            jTextArea_Alert.setText("Go to a folder to view Images.");
        }
    }
    
    public void add(){
        try{
            imageName = imagesList[pos];
            String aPath = textField_Path+"/"+imageName;
            combineList.add(aPath);     
            jTextArea_Alert.setText(imageName+" Added.");
        }
        catch(NullPointerException e){
            jTextArea_Alert.setText("Open a Folder and to Add some Images.");
        }
      
    }
    
    public void delete(){
        try{
            if(pos < imagesList.length){
                imageName = imagesList[pos];
                int i;
                String aPath = textField_Path+"/"+imageName;
                for(i=0;(i < combineList.size())&&(!combineList.get(i).equals(aPath));i++){
                    
                }
                if(combineList.get(i).equals(aPath)){
                    combineList.remove(i);
                    jTextArea_Alert.setText(imageName+" removed.");
                }  
            } 
            else {
                jTextArea_Alert.setText("This Image"+imageName+" was not Added before.");
            }
        }
        catch(NullPointerException | IndexOutOfBoundsException e){
            jTextArea_Alert.setText("Add some Image to Delete.");
            
        }
    }
    
    public void combine(){
        if(!combineList.isEmpty()){    
            if(jTextField_Class.getText().equals("") | jTextField_Subject.getText().equals("") | jTextField_StudentID.getText().equals("")){
                jTextArea_Alert.setText("Fill all the Fields with respective information and the Press Combine.");
            }
            else{
                if(jTextField_Save.getText().equals("")){
                    JFileChooser saveChooser = new JFileChooser();
        
                   saveChooser.setDialogTitle("Select Folder");
                    saveChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    saveChooser.setAcceptAllFileFilterUsed(false);

                    if (saveChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File iFile = saveChooser.getSelectedFile();
                        jTextField_Save.setText(iFile.getAbsolutePath());
                    }
                }
                outputName = (jTextField_Class.getText()+"_"+jTextField_Subject.getText()+"_"+jTextField_StudentID.getText()+".pdf");
                outputPath = jTextField_Save.getText();
        
                try {
                    Combine.combine();
                    jTextArea_Alert.setText("Images have been Combined into a Single PDF and is saved at "+outputPath+"." );
                    lastVisited_Path = combineList.get(combineList.size()-1); 
                } 
                catch (DocumentException | IOException | ArrayIndexOutOfBoundsException ex) {
                    Logger.getLogger(CombinerWindow.class.getName()).log(Level.SEVERE, null, ex);
                    jTextArea_Alert.setText("Add some images to Combine.");
                }
                combineList.clear();
                jTextField_Class.setText("");
                jTextField_Subject.setText("");
                jTextField_StudentID.setText("");
            }
        }
        else{
            jTextArea_Alert.setText("Yep! You haven't added atleast an Image.");
        }
        
    }
    
    public void save(){
        try {
            String path = CombinerWindow.lastVisited_Path;
            File file = new  File("LastVisited.txt");

        // if file doesnt exists, then create it
            if (!file.exists()) {
            file.createNewFile();
            }
            System.out.println(""+file.getAbsoluteFile());
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(path);
                bw.close();
            }

            System.out.println("Done");
            jTextArea_Alert.setText("Progress has been saved(image next to the last image in the combined set).");

        } 
            catch (IOException | NullPointerException e) {
                jTextArea_Alert.setText("Combine Some Images to Save the Session.");
                
            }
    }
    
    public void resume() throws FileNotFoundException, IOException{
        File file = new File("LastVisited.txt");
        FileReader fr = new FileReader(file);
        String line;
        try (BufferedReader br = new BufferedReader(fr)) {
            line = br.readLine();
            br.close();
        }
        jTextArea_Alert.setText("After Resuming the Work If you press Open or Go, you won't be able resume.");
        try{
            String[] split = line.split("/");
            System.out.println(split[0]);
            System.out.println(split[1]);
            String resumePath = split[0];
            jTextField_Path.setText(resumePath);
            textField_Path = resumePath;
            int i;
            imagesList = getImages(resumePath); 
            imageName = split[1];
            for(i=0;(!imagesList[i].equals(imageName) );i++){}
            i++;
            imageName = imagesList[i];
            pos= i;
            ImageIcon icon = new ImageIcon(resumePath+"/"+imageName);
            Image image = icon.getImage().getScaledInstance(jLabel_Image.getWidth(), jLabel_Image.getHeight(), Image.SCALE_SMOOTH);
            jLabel_Image.setIcon(new ImageIcon(image));
            jTextField_ImageName.setText(imageName);
            }
        catch(NullPointerException e){
            jTextArea_Alert.setText("You have no Saved activity to Resume");
        }
    }
    
    public void clearsave(){
        try {
            String path = "";
            File file = new  File("LastVisited.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            System.out.println(""+file.getAbsoluteFile());
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(path);
            }

            System.out.println("Done");

        }
        catch (IOException e) {
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog_Save = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton_Cancel = new javax.swing.JButton();
        jButton_DialogSave = new javax.swing.JButton();
        jButton_ExitWoS = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel_Image = new javax.swing.JLabel();
        jTextField_Subject = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_Alert = new javax.swing.JTextArea();
        jTextField_StudentID = new javax.swing.JTextField();
        jTextField_Path = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel_Class = new javax.swing.JLabel();
        jTextField_Save = new javax.swing.JTextField();
        jLabel_Subject = new javax.swing.JLabel();
        jButton_Combine = new javax.swing.JButton();
        jLabel_StudentID = new javax.swing.JLabel();
        jLabel_InputFolder = new javax.swing.JLabel();
        jButton_Open = new javax.swing.JButton();
        jButton_Resume = new javax.swing.JButton();
        jButton_Save = new javax.swing.JButton();
        jTextField_Class = new javax.swing.JTextField();
        jButton_Next = new javax.swing.JButton();
        jButton_Add = new javax.swing.JButton();
        jButton_Back = new javax.swing.JButton();
        jButton_Delete = new javax.swing.JButton();
        jTextField_ImageName = new javax.swing.JTextField();
        jButton_Exit = new javax.swing.JButton();
        jButton_Check = new javax.swing.JButton();

        jDialog_Save.setTitle("Save_Progress");
        jDialog_Save.setAlwaysOnTop(true);
        jDialog_Save.setBounds(new java.awt.Rectangle(500, 500, 650, 300));
        jDialog_Save.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jDialog_Save.setLocation(new java.awt.Point(444, 290));
        jDialog_Save.setName("Save"); // NOI18N
        jDialog_Save.setPreferredSize(new java.awt.Dimension(340, 180));
        jDialog_Save.setResizable(false);
        jDialog_Save.setSize(new java.awt.Dimension(340, 180));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Calibri Light", 0, 10)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Press Save and Exit,if you want to resume your work");
        jScrollPane3.setViewportView(jTextArea1);

        jButton_Cancel.setText("Cancel");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelActionPerformed(evt);
            }
        });

        jButton_DialogSave.setText("Save & Exit");
        jButton_DialogSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DialogSaveActionPerformed(evt);
            }
        });

        jButton_ExitWoS.setText("Exit Without Saving");
        jButton_ExitWoS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ExitWoSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog_SaveLayout = new javax.swing.GroupLayout(jDialog_Save.getContentPane());
        jDialog_Save.getContentPane().setLayout(jDialog_SaveLayout);
        jDialog_SaveLayout.setHorizontalGroup(
            jDialog_SaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_SaveLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog_SaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDialog_SaveLayout.createSequentialGroup()
                        .addComponent(jButton_Cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ExitWoS)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_DialogSave))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jDialog_SaveLayout.setVerticalGroup(
            jDialog_SaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog_SaveLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialog_SaveLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Cancel)
                    .addComponent(jButton_ExitWoS)
                    .addComponent(jButton_DialogSave))
                .addGap(0, 69, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("imageJoin");
        setPreferredSize(new java.awt.Dimension(1024, 711));
        setSize(new java.awt.Dimension(1024, 669));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1024, 669));

        jPanel1.setPreferredSize(new java.awt.Dimension(1024, 669));

        jLabel_Image.setBackground(new java.awt.Color(0, 0, 0));
        jLabel_Image.setToolTipText("Image Display Area");
        jLabel_Image.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jLabel_Image.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jLabel_ImageFocusGained(evt);
            }
        });

        jTextField_Subject.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jTextField_Subject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_SubjectActionPerformed(evt);
            }
        });

        jTextArea_Alert.setEditable(false);
        jTextArea_Alert.setColumns(20);
        jTextArea_Alert.setFont(new java.awt.Font("Calibri Light", 1, 13)); // NOI18N
        jTextArea_Alert.setLineWrap(true);
        jTextArea_Alert.setRows(5);
        jTextArea_Alert.setToolTipText("Displays Information");
        jTextArea_Alert.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTextArea_Alert);

        jTextField_StudentID.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jTextField_StudentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_StudentIDActionPerformed(evt);
            }
        });

        jTextField_Path.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jTextField_Path.setToolTipText("Enter the Open folder location manually  and Press Go\n OR \nPress Open to Select Folder");
        jTextField_Path.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_PathActionPerformed(evt);
            }
        });

        jLabel1.setText("Save Location");

        jLabel_Class.setText("Class");

        jTextField_Save.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jTextField_Save.setToolTipText("Edit Manually the destination path ");

        jLabel_Subject.setText("Subject");

        jButton_Combine.setText("Combine");
        jButton_Combine.setToolTipText("Press to Combine the added Images");
        jButton_Combine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CombineActionPerformed(evt);
            }
        });

        jLabel_StudentID.setText("StudentID");

        jLabel_InputFolder.setText("Open Location");

        jButton_Open.setText("Open");
        jButton_Open.setToolTipText("Press to Select a Folder");
        jButton_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_OpenActionPerformed(evt);
            }
        });

        jButton_Resume.setText("Resume Progess");
        jButton_Resume.setToolTipText("Press to Resume to the Image next to last Image \nin the Previous Output File");
        jButton_Resume.setPreferredSize(new java.awt.Dimension(95, 20));
        jButton_Resume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ResumeActionPerformed(evt);
            }
        });

        jButton_Save.setText("Save Progress");
        jButton_Save.setToolTipText("Press to Save the Image next to the last Image \nin Previous Output File");
        jButton_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SaveActionPerformed(evt);
            }
        });

        jTextField_Class.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jTextField_Class.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_ClassActionPerformed(evt);
            }
        });

        jButton_Next.setText("Next");
        jButton_Next.setToolTipText("Views the Next Image");
        jButton_Next.setMaximumSize(new java.awt.Dimension(75, 29));
        jButton_Next.setMinimumSize(new java.awt.Dimension(75, 29));
        jButton_Next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NextActionPerformed(evt);
            }
        });

        jButton_Add.setText("Add");
        jButton_Add.setToolTipText("Press to Add Images to the Output file this previews the Next Image on its own");
        jButton_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AddActionPerformed(evt);
            }
        });

        jButton_Back.setText("Back");
        jButton_Back.setToolTipText("Views the Previous Image");
        jButton_Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BackActionPerformed(evt);
            }
        });

        jButton_Delete.setText("Delete");
        jButton_Delete.setToolTipText("Press to Delete the Displaying Image , If added before ");
        jButton_Delete.setMaximumSize(new java.awt.Dimension(75, 29));
        jButton_Delete.setMinimumSize(new java.awt.Dimension(75, 29));
        jButton_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DeleteActionPerformed(evt);
            }
        });

        jTextField_ImageName.setEditable(false);
        jTextField_ImageName.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        jTextField_ImageName.setToolTipText("Displays the Name of the Image");

        jButton_Exit.setText("Exit");
        jButton_Exit.setToolTipText("Can only Exit by this button");
        jButton_Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ExitActionPerformed(evt);
            }
        });

        jButton_Check.setText("Check");
        jButton_Check.setToolTipText("Press to check which Images you have added");
        jButton_Check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_Resume, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Save)
                        .addGap(36, 36, 36)
                        .addComponent(jButton_Check, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Combine))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel_InputFolder)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_StudentID)
                                .addComponent(jLabel1)
                                .addComponent(jLabel_Subject)
                                .addComponent(jLabel_Class)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField_Path, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_Open))
                            .addComponent(jTextField_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_Subject, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_Class, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_Back)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Next, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField_ImageName, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Exit)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_Class, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_Class))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_Subject, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_Subject))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_StudentID, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_StudentID))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_InputFolder)
                            .addComponent(jTextField_Path, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_Open, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_Resume, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_Save, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_Check, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_Combine, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_Exit))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_Back, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton_Next, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField_ImageName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton_Add, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_ImageFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jLabel_ImageFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel_ImageFocusGained

    private void jTextField_SubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_SubjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_SubjectActionPerformed

    private void jTextField_PathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_PathActionPerformed
        go();
    }//GEN-LAST:event_jTextField_PathActionPerformed

    private void jButton_CombineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CombineActionPerformed
        combine();
    }//GEN-LAST:event_jButton_CombineActionPerformed

    private void jButton_OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_OpenActionPerformed
        JFileChooser openChooser = new JFileChooser();

        openChooser.setDialogTitle("Select Folder");
        openChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        openChooser.setAcceptAllFileFilterUsed(false);

        if (openChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File iFile = openChooser.getSelectedFile();
            jTextField_Path.setText(iFile.getAbsolutePath());
            go();
        }
        else {
            jTextArea_Alert.setText("No Selection ");
        }
    }//GEN-LAST:event_jButton_OpenActionPerformed

    private void jButton_ResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ResumeActionPerformed
        try {
            resume();
        } catch (IOException ex) {
            Logger.getLogger(CombinerWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton_ResumeActionPerformed

    private void jButton_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SaveActionPerformed
        save();
    }//GEN-LAST:event_jButton_SaveActionPerformed

    private void jTextField_ClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_ClassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_ClassActionPerformed

    private void jButton_NextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NextActionPerformed
        next();
    }//GEN-LAST:event_jButton_NextActionPerformed

    private void jButton_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AddActionPerformed
        add();
        next();
    }//GEN-LAST:event_jButton_AddActionPerformed

    private void jButton_BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BackActionPerformed
        back();
    }//GEN-LAST:event_jButton_BackActionPerformed

    private void jButton_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DeleteActionPerformed
        delete();
    }//GEN-LAST:event_jButton_DeleteActionPerformed

    private void jButton_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelActionPerformed

        jDialog_Save.setVisible(false); 
        
    }//GEN-LAST:event_jButton_CancelActionPerformed

    private void jButton_DialogSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DialogSaveActionPerformed
        save();
        status = false;
        jDialog_Save.setVisible(status);
        System.exit(0);
    }//GEN-LAST:event_jButton_DialogSaveActionPerformed

    private void jButton_ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ExitActionPerformed
        jDialog_Save.setVisible(true);
        jDialog_Save.toFront();
    }//GEN-LAST:event_jButton_ExitActionPerformed

    private void jButton_ExitWoSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ExitWoSActionPerformed
        clearsave();
        combineList.clear();
        System.exit(0);
    }//GEN-LAST:event_jButton_ExitWoSActionPerformed

    private void jButton_CheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CheckActionPerformed
        try{
            jTextArea_Alert.setText(combineList.toString()+" will be combined to form the Output file");
            
        }
        catch(NullPointerException e){
            jTextArea_Alert.setText("Yep! add some Images to Check");
        }
    }//GEN-LAST:event_jButton_CheckActionPerformed

    private void jTextField_StudentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_StudentIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_StudentIDActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CombinerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CombinerWindow().setVisible(true);
                jDialog_Save.setVisible(false);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Add;
    private javax.swing.JButton jButton_Back;
    private javax.swing.JButton jButton_Cancel;
    private javax.swing.JButton jButton_Check;
    private javax.swing.JButton jButton_Combine;
    private javax.swing.JButton jButton_Delete;
    private javax.swing.JButton jButton_DialogSave;
    private javax.swing.JButton jButton_Exit;
    private javax.swing.JButton jButton_ExitWoS;
    private javax.swing.JButton jButton_Next;
    private javax.swing.JButton jButton_Open;
    private javax.swing.JButton jButton_Resume;
    private javax.swing.JButton jButton_Save;
    private static javax.swing.JDialog jDialog_Save;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel_Class;
    private javax.swing.JLabel jLabel_Image;
    private javax.swing.JLabel jLabel_InputFolder;
    private javax.swing.JLabel jLabel_StudentID;
    private javax.swing.JLabel jLabel_Subject;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea_Alert;
    private javax.swing.JTextField jTextField_Class;
    private javax.swing.JTextField jTextField_ImageName;
    private javax.swing.JTextField jTextField_Path;
    private javax.swing.JTextField jTextField_Save;
    private javax.swing.JTextField jTextField_StudentID;
    private javax.swing.JTextField jTextField_Subject;
    // End of variables declaration//GEN-END:variables
    static String textField_Path;
}
