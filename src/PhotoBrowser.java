import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

public class PhotoBrowser {
    private JFrame frame;
    private JLabel statusBar;
    private PhotoBrowserModel model;
    private PhotoBrowserView view;
    private PhotoBrowserController controller;

    public PhotoBrowser() {
        frame = new JFrame("PhotoBrowser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(800, 500));
        frame.setMinimumSize(new Dimension(800, 500));

        JPanel mainPanel = new JPanel();

        model = new PhotoBrowserModel();
        view = new PhotoBrowserView(mainPanel);
        controller = new PhotoBrowserController(model, view);

        mainPanel.setPreferredSize(new Dimension(600, 400));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.add(mainPanel, BorderLayout.CENTER);

        statusBar = new JLabel();
        frame.add(statusBar, BorderLayout.SOUTH);

        createMenuBar();
        createToolBar();

        frame.setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = getFileMenu();
        menuBar.add(fileMenu);

        JMenu viewMenu = getViewMenu();
        menuBar.add(viewMenu);
    }

    private JMenu getFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem importItem = new JMenuItem("Import");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem quitItem = new JMenuItem("Quit");

        importItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importFile();
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFile();
            }
        });

        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitSystem();
            }
        });

        fileMenu.add(importItem);
        fileMenu.add(deleteItem);
        fileMenu.add(quitItem);

        return fileMenu;
    }

    private void importFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            statusBar.setText(fileChooser.getSelectedFile().getName() + " is selected");

            controller.importPhoto(filePath);
        }
    }

    private void exitSystem() {
        System.exit(0);
    }

    private void deleteFile() {
        statusBar.setText("Delete is selected");
    }

    private JMenu getViewMenu() {
        JMenu viewMenu = new JMenu("View");
        JRadioButtonMenuItem photoViewerItem = new JRadioButtonMenuItem("Photo Viewer", true);
        JRadioButtonMenuItem browserItem = new JRadioButtonMenuItem("Browser");

        viewMenu.add(photoViewerItem);
        viewMenu.add(browserItem);

        ButtonGroup viewGroup = new ButtonGroup();

        viewGroup.add(photoViewerItem);
        viewGroup.add(browserItem);

        photoViewerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOnePhoto();
            }
        });

        browserItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGrid();
            }
        });
        return viewMenu;
    }

    private void showOnePhoto() {
        statusBar.setText("Show one photo at a time");
    }

    private void showGrid() {
        statusBar.setText("Show a grid of thumbnails");
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        frame.add(toolBar, BorderLayout.WEST);

        JButton colorButton = new JButton("Change Color");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeAnnotationColor();
            }
        });

        JToggleButton peopleButton = new JToggleButton("People");
        JToggleButton placesButton = new JToggleButton("Places");
        JToggleButton schoolButton = new JToggleButton("School");

        toolBar.add(colorButton);
        toolBar.add(peopleButton);
        toolBar.add(placesButton);
        toolBar.add(schoolButton);

        peopleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activatePeopleButton();
            }
        });

        placesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activatePlaceButton();
            }
        });

        schoolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activateSchoolButton();
            }
        });
    }

    private void changeAnnotationColor() {
        PhotoComponent photoComponent = view.getCurrentPhotoComponent();
        boolean isPhotoComponent = photoComponent != null;

        if (isPhotoComponent) {
            Annotation selectedAnnotation = photoComponent.getSelectedAnnotation();
            boolean isSelectedAnnotation = selectedAnnotation != null;

            if (isSelectedAnnotation) {
                Color newColor = JColorChooser.showDialog(frame, "Choose Color", Color.BLACK);
                boolean isColorChosen = newColor != null;

                if (isColorChosen) {
                    photoComponent.changeSelectedAnnotationColor(newColor);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No drawing selected.");
            }
        }
    }

    private boolean isPeopleSelected = false;
    private boolean isPlaceSelected = false;
    private boolean isSchoolSelected = false;

    private void activatePeopleButton() {
        switchPeopleSelectedStatus();
        String peopleStatus = isPeopleSelected ? "People is selected" : "People is unselected";
        statusBar.setText(peopleStatus);
    }

    private void activatePlaceButton() {
        switchPlaceSelectedStatus();
        String placeStatus = isPlaceSelected ? "Place is selected" : "Place is unselected";
        statusBar.setText(placeStatus);
    }

    private void activateSchoolButton() {
        switchSchoolSelectedStatus();
        String schoolStatus = isSchoolSelected ? "School is selected" : "School is unselected";
        statusBar.setText(schoolStatus);
    }

    private void switchPeopleSelectedStatus() {
        isPeopleSelected = !isPeopleSelected;
    }
    private void switchPlaceSelectedStatus() {
        isPlaceSelected = !isPlaceSelected;
    }
    private void switchSchoolSelectedStatus() {
        isSchoolSelected = !isSchoolSelected;
    }
}
