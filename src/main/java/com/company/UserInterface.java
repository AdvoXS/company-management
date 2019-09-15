package com.company;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserInterface extends JFrame {
    //Window properties
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int widthWindow = 1100;
    private int heightWindow = 550;
    private final String [] activeWindow = {"Persons", "Posts", "Departments"};
    private String activeWindowNow;
    //DB
    private DataBaseControl dataBaseControl = null;

    //elements window
    private Container contents;
    private JButton addButton;
    private JButton updateButton;
    private JButton changeButton;
    private JButton deleteButton;

    private JButton personsWindowButton;
    private JButton postsWindowButton;
    private JButton departmentsButton;

    private DefaultTableModel personTableModel;
    private DefaultTableModel postTableModel;
    private DefaultTableModel departmentTableModel;

    private JTable personsTable; //Table

    private Container containerButtons;
    private Container containerWindowButtons;

    public UserInterface(DataBaseControl dataBaseControl) throws SQLException
    {
        super("Company management");
        this.setBounds((screenSize.width-widthWindow)/2,(screenSize.height-heightWindow)/2, widthWindow, heightWindow);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.dataBaseControl = dataBaseControl; //aggregation object

        activeWindowNow = activeWindow[0];//window persons is active now

        initElements();

    }

    private void showAllPosts()throws SQLException //Show Table Posts
    {
        dataBaseControl.getAllPostsSQL();//before get actual data of post
        ArrayList<Post> tempPostsList = dataBaseControl.getPostsList();

        String arrayPosts [][] = new String[tempPostsList.size()][2];
        String [] columnsHeader = new String[] {"Должность", "Зарплата"};
        if(postTableModel==null)
        {
            postTableModel = new DefaultTableModel();
            for(String c : columnsHeader)
            {
                postTableModel.addColumn(c);
            }
        }
        postTableModel.setRowCount(0);//clear table for updates
        for (int i = 0; i < tempPostsList.size(); i++) {
            arrayPosts[i][0] = tempPostsList.get(i).getName_post();
            arrayPosts[i][1] = Integer.toString(tempPostsList.get(i).getSalary());
            postTableModel.addRow(new Object[]{arrayPosts[i][0], arrayPosts[i][1]});
        }
        if(!personsTable.getModel().equals(postTableModel)){
            personsTable.setModel(postTableModel);
        }
    }
    private  void showAllDepartments()throws SQLException //Show Table Departments
    {
        dataBaseControl.getAllDepartmentsSQL();
        ArrayList<Department> tempDepartmentsList = dataBaseControl.getDepartmentsList();

        String arrayDepartments [][] = new String[tempDepartmentsList.size()][3];
        String [] columnsHeader = new String[] {"Отдел", "Номер телефона", "Email"};
        if(departmentTableModel==null)
        {
            departmentTableModel = new DefaultTableModel();
            for(String c : columnsHeader)
            {
                departmentTableModel.addColumn(c);
            }
        }
        departmentTableModel.setRowCount(0);//clear table for updates

        for (int i = 0; i < tempDepartmentsList.size(); i++) {
            arrayDepartments[i][0] = tempDepartmentsList.get(i).getName_department();
            arrayDepartments[i][1] = Long.toString(tempDepartmentsList.get(i).getNumber_phone());
            arrayDepartments[i][2] = tempDepartmentsList.get(i).getEmail();
            departmentTableModel.addRow(new Object[]{arrayDepartments[i][0], arrayDepartments[i][1],arrayDepartments[i][2]});
        }
        if(!personsTable.getModel().equals(departmentTableModel)){
            personsTable.setModel(departmentTableModel);
        }
    }
    private void showAllPersons() throws SQLException //Show Table Persons
    {
        dataBaseControl.getAllPersonsSQL();//before get actual data of persons

        ArrayList<Person> tempPersonList = dataBaseControl.getPersonsList();
        String arrayPersons [][] = new String[tempPersonList.size()][5];

        String [] columnsHeader = new String[] {"Id сотрудника", "Имя",
                "Фамилия", "Должность", "Отдел"}; //Header Table
        if(personTableModel==null)
        {
            personTableModel = new DefaultTableModel();
            for(String c : columnsHeader)
            {
                personTableModel.addColumn(c);
            }
        }
        personTableModel.setRowCount(0);//clear table for updates

            for (int i = personTableModel.getRowCount(); i < tempPersonList.size(); i++) {
                arrayPersons[i][0] = tempPersonList.get(i).getId_person();
                arrayPersons[i][1] = tempPersonList.get(i).getName();
                arrayPersons[i][2] = tempPersonList.get(i).getLast_name();
                arrayPersons[i][3] = tempPersonList.get(i).getName_post();
                arrayPersons[i][4] = tempPersonList.get(i).getName_department();
                personTableModel.addRow(new Object[]{arrayPersons[i][0], arrayPersons[i][1], arrayPersons[i][2], arrayPersons[i][3], arrayPersons[i][4]});
            }

        if( personsTable==null ) { //init table & components
            personsTable = new JTable(personTableModel);
            personsTable.setRowHeight(30);
            personsTable.setDefaultEditor(Object.class, null);//readonly table
            contents.add(new JScrollPane(personsTable));
            setContentPane(contents);
            getContentPane().add(containerButtons);
        }
        if(!personsTable.getModel().equals(personTableModel)){
            personsTable.setModel(personTableModel);
        }

    }

    private void addPerson(String name, String last_name, String post, String department) throws SQLException //method add person in DB
    {
        int max; //max id for create new id
        dataBaseControl.getAllPersonsSQL();//update data
        if(dataBaseControl.getPersonsList().size()!=0)
        {
         max = Integer.parseInt(dataBaseControl.getPersonsList().get(0).getId_person());
         for(int i =1;i<dataBaseControl.getPersonsList().size();i++) {
           if(max<Integer.parseInt(dataBaseControl.getPersonsList().get(i).getId_person()))
           {
               max =Integer.parseInt(dataBaseControl.getPersonsList().get(i).getId_person());
           }
        }
        }
        else max=0;
        dataBaseControl.addPersonSQL(Integer.toString(max+1), name,
                last_name,post,department);
    }
    private void initElements() throws SQLException
    {
        contents = new Box(BoxLayout.Y_AXIS);
        init_Buttons();
        showAllPersons();
    }
    private void init_Buttons()
    {
        containerButtons = getContentPane();
        containerButtons.setLayout(new FlowLayout(FlowLayout.CENTER));

        containerWindowButtons = getContentPane();
        containerWindowButtons.setLayout(new FlowLayout(FlowLayout.CENTER));

        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { //init events for Add Button

                if(activeWindowNow == activeWindow[0]) //work with Table of persons
                    addButtonEvent(0);
                else if(activeWindowNow == activeWindow[1]) //work with Table of posts
                    addButtonEvent(1);
                else if(activeWindowNow == activeWindow[2]) //work with Table of departments
                    addButtonEvent(2);

            }
        });

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() { //init events for Update Button
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(activeWindowNow == activeWindow[0])updateButtonEvent(0);
                else if(activeWindowNow == activeWindow[1])updateButtonEvent(1);
                else if(activeWindowNow == activeWindow[2])updateButtonEvent(2);
            }
        });

        changeButton = new JButton("Change");
        changeButton.addActionListener(new ActionListener() { //init events for Change Button
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(activeWindowNow == activeWindow[0])changeButtonEvent(0);
                else if(activeWindowNow == activeWindow[1])changeButtonEvent(1);
                else if(activeWindowNow == activeWindow[2])changeButtonEvent(2);
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {//init events for Delete Button
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(activeWindowNow == activeWindow[0]) deleteButtonEvent(0);
                else if(activeWindowNow == activeWindow[1])deleteButtonEvent(1);
                else if(activeWindowNow == activeWindow[2])deleteButtonEvent(2);
            }
        });
        personsWindowButton = new JButton("Сотрудники");
        personsWindowButton.setEnabled(false);
        personsWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseWindow(0);
            }
        });
        postsWindowButton = new JButton("Должности");
        postsWindowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseWindow(1);
            }
        });
        departmentsButton = new JButton("Отделы");
        departmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chooseWindow(2);
            }
        });

        containerWindowButtons.add(personsWindowButton);
        containerWindowButtons.add(postsWindowButton);
        containerWindowButtons.add(departmentsButton);

        containerButtons.add(addButton);
        containerButtons.add(updateButton);
        containerButtons.add(changeButton);
        containerButtons.add(deleteButton);
    }

    private void updateButtonEvent(int index)
    {
        switch (index)
        {
            case 0:
                try {
                    showAllPersons();
                    JOptionPane.showMessageDialog(contents, "Получены актуальные данные о сотрудниках.");
                }
                catch(SQLException e)
                {
                    JOptionPane.showMessageDialog(contents, "Ошибка. Соединение с базой данных не установлено.");
                }
                break;
            case 1:
                try {
                    showAllPosts();
                    JOptionPane.showMessageDialog(contents, "Получены актуальные данные о должностях.");
                }
                catch(SQLException e)
                {
                    JOptionPane.showMessageDialog(contents, "Ошибка. Соединение с базой данных не установлено.");
                }
                break;
            case 2:
                try {
                    showAllDepartments();
                    JOptionPane.showMessageDialog(contents, "Получены актуальные данные об отделах.");
                }
                catch(SQLException e)
                {
                    JOptionPane.showMessageDialog(contents, "Ошибка. Соединение с базой данных не установлено.");
                }
                break;
        }
    }
    private void addButtonEvent(int index)
    {

        JButton button = new JButton("ADD");
        JFrame addWindow = new JFrame();
        addWindow.setBounds((screenSize.width-450)/2,(screenSize.height-250)/2, 450, 250);
        addWindow.setVisible(true);
        Container container = new Container();
        container.setLayout(null);
        switch (index)
        {
            case 0:
                showPersonWindow(button,addWindow,container,false,null);
                break;
            case 1:
                showPostWindow(button,addWindow,container,false,null);
                break;
            case 2:
                showDepartmentWindow(button,addWindow,container,false,null);
                break;
        }
    }
    private void deleteButtonEvent(int index)
    {
        switch (index){
            case 0:
                if(personsTable.getSelectedRow()!=-1) {
                    try {
                        dataBaseControl.deletePersonSQL(personTableModel.getValueAt(personsTable.getSelectedRow(), 0).toString());
                        JOptionPane.showMessageDialog(contents, "Данные сотрудника с id: "+personTableModel.getValueAt(personsTable.getSelectedRow(), 0).toString()+" удалены!");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(contents, "Не удалось удалить сотрудника. Повторите позже.");
                    }
                    try {
                        showAllPersons();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(contents, "Не удалось установить соединение с базой данных Повторите позже.");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(contents, "Выделите сотрудника, которого хотите удалить из базы данных нажатием мыши по его данным.");
                }
                break;
            case 1:
                if(personsTable.getSelectedRow()!=-1) {
                    try {
                        dataBaseControl.deletePostSQL(personsTable.getValueAt(personsTable.getSelectedRow(),0).toString());
                        JOptionPane.showMessageDialog(contents, "Данные о должности удалены! Проверьте сотрудников, которые находились на данной должности.");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(contents, "Не удалось удалить должность. Повторите позже.");
                    }
                    try {
                        showAllPosts();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(contents, "Не удалось установить соединение с базой данных Повторите позже.");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(contents, "Выделите должность, которую хотите удалить из базы данных нажатием мыши по ее данным.");
                }
                break;
            case 2:
                if(personsTable.getSelectedRow()!=-1) {
                    try {
                        dataBaseControl.deleteDepartmentSQL((personsTable.getValueAt(personsTable.getSelectedRow(),0).toString()));
                        JOptionPane.showMessageDialog(contents, "Данные об отделе удалены! Проверьте сотрудников, которые находились в данном отдел.");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(contents, "Не удалось удалить отдел. Повторите позже.");
                    }
                    try {
                        showAllDepartments();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(contents, "Не удалось установить соединение с базой данных Повторите позже.");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(contents, "Выделите отдел, который хотите удалить из базы данных нажатием мыши по ее данным.");
                }
                break;
        }
    }


    private void changeButtonEvent(int index)
    {
        JButton button = new JButton("Change");
        JFrame changeWindow = new JFrame();
        changeWindow.setBounds((screenSize.width-450)/2,(screenSize.height-250)/2, 450, 250);

        Container container = new Container();
        container.setLayout(null);
        switch (index){
            case 0:
                if(personsTable.getSelectedRow()!=-1)
                {
                    String[] personInfo = {
                            personTableModel.getValueAt(personsTable.getSelectedRow(), 0).toString(),
                            personTableModel.getValueAt(personsTable.getSelectedRow(), 1).toString(),
                            personTableModel.getValueAt(personsTable.getSelectedRow(), 2).toString(),
                            (personTableModel.getValueAt(personsTable.getSelectedRow(), 3)==null)?null: personTableModel.getValueAt(personsTable.getSelectedRow(), 3).toString(),
                            ( personTableModel.getValueAt(personsTable.getSelectedRow(), 4)==null) ?null :personTableModel.getValueAt(personsTable.getSelectedRow(), 4).toString()
                    };
                    changeWindow.setVisible(true);
                    showPersonWindow(button,changeWindow,container,true, personInfo );
                }
                else {
                    JOptionPane.showMessageDialog(contents, "Выделите сотрудника, данные которого хотите изменть в базе данных нажатием мыши по его данным.");
                }
                break;
            case 1:
                if(personsTable.getSelectedRow()!=-1)
                {
                    String[] postInfo = {
                            postTableModel.getValueAt(personsTable.getSelectedRow(), 0).toString(),
                            postTableModel.getValueAt(personsTable.getSelectedRow(), 1).toString()
                    };
                    changeWindow.setVisible(true);
                    showPostWindow(button,changeWindow,container,true, postInfo) ;
                }
                else {
                    JOptionPane.showMessageDialog(contents, "Выделите должность, данные которой хотите изменть в базе данных нажатием мыши по его данным.");
                }
                break;
            case 2:
                if(personsTable.getSelectedRow()!=-1)
                {
                    String[] postInfo = {
                            departmentTableModel.getValueAt(personsTable.getSelectedRow(), 0).toString(),
                            departmentTableModel.getValueAt(personsTable.getSelectedRow(), 1).toString(),
                            departmentTableModel.getValueAt(personsTable.getSelectedRow(), 2).toString(),
                    };
                    changeWindow.setVisible(true);
                    showDepartmentWindow(button,changeWindow,container,true, postInfo) ;
                }
                else {
                    JOptionPane.showMessageDialog(contents, "Выделите отдел, данные которого хотите изменть в базе данных нажатием мыши по его данным.");
                }
                break;
        }
    }


    private void chooseWindow(int index)
    {
        switch (index) {
            case 0: {
                activeWindowNow = activeWindow[0];
                personsWindowButton.setEnabled(false);
                postsWindowButton.setEnabled(true);
                departmentsButton.setEnabled(true);
                try {
                    showAllPersons();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case 1:{
                activeWindowNow=activeWindow[1];
                personsWindowButton.setEnabled(true);
                postsWindowButton.setEnabled(false);
                departmentsButton.setEnabled(true);
                try{
                    showAllPosts();
                }
                catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            }
            case 2:{
                activeWindowNow=activeWindow[2];
                personsWindowButton.setEnabled(true);
                postsWindowButton.setEnabled(true);
                departmentsButton.setEnabled(false);
                try {
                    showAllDepartments();
                }
                catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
    }


    private void showPersonWindow(JButton button, JFrame window, Container container,boolean isChange,String personInfo [])
    {

        JTextField nameTextField = new JTextField(15);
        JLabel nameLabel = new JLabel("Имя");
        nameLabel.setBounds(10,10,80,20);
        nameTextField.setBounds(90,10,305,20);

        JTextField last_nameTextField = new JTextField(30);
        JLabel last_nameLabel = new JLabel("Фамилия");
        last_nameLabel.setBounds(10,50,80,20);
        last_nameTextField.setBounds(90,50,305,20);

        JTextField namePostTextField = new JTextField(30);
        JLabel namePostLabel = new JLabel("Должность");
        namePostLabel.setBounds(10,90,80,20);
        namePostTextField.setBounds(90,90,305,20);

        JTextField nameDepartmentField = new JTextField(30);
        JLabel nameDepartmentLabel = new JLabel("Отдел");
        nameDepartmentLabel.setBounds(10,130,80,20);
        nameDepartmentField.setBounds(90,130,305,20);

        button.setBounds(180,165,100,40);
        container.add(nameLabel);
        container.add(nameDepartmentLabel);
        container.add(namePostLabel);
        container.add(last_nameLabel);
        container.add(nameTextField);
        container.add(last_nameTextField);
        container.add(namePostTextField);
        container.add(nameDepartmentField);

        if(isChange){ //this window is change
            nameTextField.setText(personInfo[1]);
            last_nameTextField.setText((personInfo[2]));
            namePostTextField.setText(personInfo[3]);
            nameDepartmentField.setText((personInfo[4]));
            window.setTitle("Изменение информации о сотруднике");
        }
        else{ // this window is add
            window.setTitle("Добавление информации о сотруднике");
        }

        container.add(button);
        window.setContentPane(container);
        button.addActionListener(new ActionListener() { //event button
            @Override
            public void actionPerformed(ActionEvent actionEvent)  {
                        if (!isChange && nameTextField.getText() != "" && last_nameTextField.getText() != "" && namePostTextField.getText() != "" && nameDepartmentField.getText() != "") {
                            try {
                                addPerson(nameTextField.getText(), last_nameTextField.getText(), namePostTextField.getText(), nameDepartmentField.getText());
                                showAllPersons();
                                JOptionPane.showMessageDialog(window, "Работник успешно добавлен в базу данных.");
                            }
                            catch (SQLException e){
                                JOptionPane.showMessageDialog(window,"Не удалось добавить сотрудника.\nУдостовертесь, что введенная должность и отдел существуют.\nУдостовертесь, что все поля заполнены.");
                                System.out.println(e.getMessage());
                            }
                        }
                        else if(isChange && nameTextField.getText() != "" && last_nameTextField.getText() != "" && namePostTextField.getText() != "" && nameDepartmentField.getText() != "")
                        {
                            try {
                                dataBaseControl.changePersonSQL(personInfo[0], nameTextField.getText(),last_nameTextField.getText(),namePostTextField.getText(),nameDepartmentField.getText());
                                JOptionPane.showMessageDialog(window, "Данные о сотруднике id: "+personInfo[0]+" успешно изменены.");
                                showAllPersons();
                            }
                            catch (SQLException e)
                            {
                                JOptionPane.showMessageDialog(window, " Не удалось изменить данные сотрудника id: "+personInfo[0]+". Проверьте правильность введеных данных и соединение с БД.\n" +
                                        "Введенная должность и отдел должны обязательно существовать");
                                System.out.println(e.getMessage());
                            }
                        }
            }
        });
    }

    private void showPostWindow(JButton button, JFrame window, Container container,boolean isChange,String postInfo [])
    {
        window.setBounds((screenSize.width-450)/2,(screenSize.height-180)/2, 450, 180);
        JTextField nameTextField = new JTextField(15);
        nameTextField.setBounds(90,10,305,20);
        JLabel nameLabel = new JLabel("Должность");
        nameLabel.setBounds(10,10,80,20);

        JTextField salaryTextField = new JTextField(30);
        JLabel salaryLabel = new JLabel("Зарплата");
        salaryLabel.setBounds(10,50,80,20);
        salaryTextField.setBounds(90,50,305,20);

        button.setBounds(180,85,100,40);
        container.add(nameTextField);
        container.add(salaryTextField);
        container.add(nameLabel);
        container.add(salaryLabel);
        container.add(button);
        window.setContentPane(container);
        if(isChange){
            nameTextField.setText(postInfo [0]);
            salaryTextField.setText((postInfo [1]));
            window.setTitle("Изменение информации о должности");
            nameTextField.setEnabled(false);
        }
        else{
            window.setTitle("Добавление информации о должности");
        }
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!isChange && nameTextField.getText().trim().length() > 0  && salaryTextField.getText().trim().length() > 0) {
                    try {
                        dataBaseControl.getAllPostsSQL();
                        dataBaseControl.addPostSQL(nameTextField.getText(), Integer.parseInt(salaryTextField.getText()));
                        showAllPosts();
                        JOptionPane.showMessageDialog(window, "Должность успешно добавлена в базу данных.");
                    }
                    catch (SQLException e){
                        JOptionPane.showMessageDialog(window,"Не удалось добавить должность.\nУдостовертесь, что все поля заполнены.\nУдостовертесь, что нет должностей с таким же названием.");
                        System.out.println(e.getMessage());
                    }
                }
                else if(isChange && nameTextField.getText().trim().length() > 0 && salaryTextField.getText().trim().length() > 0)
                {
                    try {
                        dataBaseControl.changePostSQL(nameTextField.getText(), Integer.parseInt(salaryTextField.getText()));
                        JOptionPane.showMessageDialog(window, "Данные о должности успешно изменены.");
                        showAllPosts();
                    }
                    catch (SQLException e)
                    {
                        JOptionPane.showMessageDialog(window,"Не удалось добавить должность.\nУдостовертесь, что все поля заполнены. В поле зарплата вводить только числовое значение\nУдостовертесь, что нет должностей с таким же названием.");
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    JOptionPane.showMessageDialog(window,"Не удалось добавить должность.\nУдостовертесь, что все поля заполнены. В поле зарплата вводить только числовое значение\nУдостовертесь, что нет должностей с таким же названием.");
                }
            }
        });
    }

    private void showDepartmentWindow(JButton button, JFrame window, Container container,boolean isChange,String departmentInfo [])
    {
        window.setBounds((screenSize.width-450)/2,(screenSize.height-180)/2, 450, 250);

        JTextField nameTextField = new JTextField(15);
        nameTextField.setBounds(90,10,305,20);
        JLabel nameLabel = new JLabel("Отдел");
        nameLabel.setBounds(10,10,80,20);

        JTextField numberTextField = new JTextField(30);
        JLabel numberLabel = new JLabel("Телефон");
        numberLabel.setBounds(10,50,80,20);
        numberTextField.setBounds(90,50,305,20);

        JTextField emailTextField = new JTextField(30);
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(10,90,80,20);
        emailTextField.setBounds(90,90,305,20);

        button.setBounds(180,135,100,40);
        container.add(nameTextField);
        container.add(numberTextField);
        container.add(nameLabel);
        container.add(numberLabel);
        container.add(emailTextField);
        container.add(emailLabel);
        container.add(button);
        window.setContentPane(container);
        if(isChange){
            nameTextField.setText(departmentInfo [0]);
            numberTextField.setText((departmentInfo [1]));
            emailTextField.setText(departmentInfo[2]);
            window.setTitle("Изменение информации об отделе");
            nameTextField.setEnabled(false);
        }
        else{
            window.setTitle("Добавление информации об отделе");
        }
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!isChange && nameTextField.getText().trim().length() > 0 && numberTextField.getText().trim().length() > 0 && emailTextField.getText().trim().length() > 0) {
                    try {
                        dataBaseControl.getAllPostsSQL();
                        dataBaseControl.addDepartmentSQL(nameTextField.getText(), Long.parseLong(numberTextField.getText()),emailTextField.getText());
                        showAllDepartments();
                        JOptionPane.showMessageDialog(window, "Отдел успешно добавлен в базу данных.");
                    }
                    catch (SQLException e){
                        JOptionPane.showMessageDialog(window,"Не удалось добавить Отдел.\nУдостовертесь, что все поля заполнены. В поле телефон вводить только числовое значение\nУдостовертесь, что нет отделов с таким же названием.");
                        System.out.println(e.getMessage());
                    }
                }
                else if(isChange && nameTextField.getText().trim().length() > 0 && numberTextField.getText().trim().length() > 0 && emailTextField.getText().trim().length() > 0)
                {
                    try {
                        dataBaseControl.changeDepartmentSQL(nameTextField.getText(), Long.parseLong(numberTextField.getText()),emailTextField.getText());
                        JOptionPane.showMessageDialog(window, "Данные об отделе успешно изменены.");
                        showAllDepartments();
                    }
                    catch (SQLException e)
                    {
                        JOptionPane.showMessageDialog(window, " Не удалось изменить данные отдела. Проверьте правильность введеных данных и соединение с БД.");
                        System.out.println(e.getMessage());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(window,"Не удалось добавить Отдел.\nУдостовертесь, что все поля заполнены. В поле телефон вводить только числовое значение\nУдостовертесь, что нет отделов с таким же названием.");
                }
            }
        });
    }

}
