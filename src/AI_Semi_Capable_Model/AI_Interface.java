package AI_Semi_Capable_Model;

import DTOS.EXTRA_Links;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AI_Interface {
    private final JFrame frame;
    List<String> apis;

    public static void main(String[] args) {
        new AI_Interface();
    }

    public AI_Interface() {
        //gets the apis of GPT-2.
        Main.getAPIS(apis);

        // Main Part - Frame
        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1000, 1000);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.GRAY);
        
        JLabel attach1 = new JLabel();
        attach1.setBackground(Color.LIGHT_GRAY);
        attach1.setOpaque(true);
        attach1.setBounds(18, 10, 950, 940);
        frame.add(attach1);
        
        JLabel attach2 = new JLabel();
        attach2.setBackground(Color.GRAY);
        attach2.setOpaque(true);
        attach2.setBounds(900, 0, 50, 900);
        attach1.add(attach2);


        // Input Provider
        JTextField nameField = new JTextField();
        nameField.setBackground(Color.GRAY);
        nameField.setForeground(Color.BLACK);
        nameField.setFont(new Font("Arial", Font.BOLD, 25));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setOpaque(true);
        nameField.setText("text...");
        nameField.setBounds(-2, 892, 902, 50);  // Set bounds for nameField
        attach1.add(nameField);


        // Input Sender
        JLabel iconLabel = new JLabel();
        iconLabel.setBackground(Color.GRAY);
        iconLabel.setForeground(Color.BLACK);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 40));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        iconLabel.setText("^");
        iconLabel.setOpaque(true);
        iconLabel.setBounds(900, 890, 50, 50);
        attach1.add(iconLabel);


        // Description Text Area
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("Hi! I am your AI Assistant that will guide you through the app." +
                "\n\nWe currently support only conversations in english." +
                "\n\nYou can ask me questions related to the application, like the ones I suggested in the tool bar." +
                "\n\nI am happy to answer to questions not so related to the topic, or try to seek resources that can help u further, although I might be banana bit slow.");
        descriptionArea.setBackground(Color.LIGHT_GRAY);
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setFont(new Font("Arial", Font.BOLD, 25));
        descriptionArea.setOpaque(true);
        descriptionArea.setFocusable(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        

        //scroll pane
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 900, 900);
        attach1.add(scrollPane);


        //clear button
        JButton clearButton = new JButton("X");
        clearButton.setBounds(900, 20, 50, 50);
        clearButton.setBackground(Color.RED);
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.getDisabledSelectedIcon();
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        attach1.add(clearButton);
        

        //configuring the interface
        attach1.setComponentZOrder(scrollPane, 0);
        attach1.setComponentZOrder(nameField, 0);
        attach1.setComponentZOrder(clearButton, 0);
        attach1.revalidate();
        attach1.repaint();


        //Listen to questions
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;

                //this handles more questions asked at the same time, placing them in a queue and answering them in order
                handleMultipleTasks(nameField, descriptionArea);
            }
        });
        
        
        descriptionArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != 1) return;
                
                try {
                    int line = descriptionArea.getLineOfOffset(descriptionArea.getCaretPosition());
                    String link = descriptionArea.getText().split("\\R")[line].split(": ")[1];
                    
                    //Unsafe (No exc. handling)
                    EXTRA_Links.accessLink(link);
                } catch (BadLocationException e2) {
                    e2.printStackTrace();
                }
            }
        });
        
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().isEmpty()) {
                    nameField.setText(RandomQuestionGenerator.getRandomQuestion());
                }
            }
            
            @Override
            public void focusGained(FocusEvent e) {
                nameField.setText("");
            }
        });
        
        // Add action listener to clear the descriptionArea when the button is clicked
        clearButton.addActionListener(e -> {
            descriptionArea.setText("");
            if (MultipleTasksHandler.getRunnables().isEmpty()) return;
            Objects.requireNonNull(MultipleTasksHandler.getRunnables().poll()).interrupt();
        });
        
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //If AI Models not used, we refine them with the previous discussions.
                Main.teachAIModelsIfNotUsed(apis);
            }
        });
    }

    private void handleMultipleTasks(JTextField nameField, JTextArea descriptionArea) {
        AtomicReference<AtomicInteger> count = new AtomicReference<>(new AtomicInteger());
        Lock mainLock = MultipleTasksHandler.getMainLock();
        Lock animationLock = MultipleTasksHandler.getAnimationLock();
        ExecutorService executorService = MultipleTasksHandler.getExecutorService();
        ArrayBlockingQueue<Thread> runnables = MultipleTasksHandler.getRunnables();

        String s = nameField.getText();
        try {
            Runnable myRunnable = () -> {
                synchronized (mainLock) {
                    if (Thread.currentThread().isInterrupted()) return;
                    String content = Main.call(s);
                    if (Thread.currentThread().isInterrupted()) return;
                    if (!runnables.contains(Thread.currentThread())) return;
                    descriptionArea.append("\n" + "-".repeat(100) + "\n" + content);
                    descriptionArea.append("\n\n" + "Ran on: " + LocalDateTime.now(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
                    descriptionArea.setForeground(Color.BLACK);

                }
            };
            Thread mainThread = new Thread(myRunnable);
            runnables.offer(mainThread);
            mainThread.start();

            Runnable animationRunnable = () -> {
                synchronized (animationLock) {
                    descriptionArea.append("\n\n");
                    while (mainThread.isAlive() && !mainThread.isInterrupted()) {
                        try {
                            if (count.get().get() == 300) {
                                mainThread.interrupt();

                                //Cleaning Data:
                                count.set(null);
                                break;
                            }

                            Thread.sleep(300);

                            count.get().getAndIncrement();
                            if (descriptionArea.getText().contains("..."))
                                descriptionArea.setText(descriptionArea.getText().replace(".", ""));
                            descriptionArea.append(".");
                        } catch (InterruptedException e2) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                descriptionArea.setText(descriptionArea.getText().replace(".", ""));
            };
            executorService.execute(animationRunnable);
        } catch(Exception exc){
            descriptionArea.setText("Bad user input!");
            frame.dispose();
        } finally {
            nameField.setText(RandomQuestionGenerator.getRandomQuestion());
            nameField.setFocusable(false);
            nameField.setFocusable(true);
        }
    }
    
    public static class RandomQuestionGenerator {
        public static String getRandomQuestion() {
            return switch (new Random().nextInt(1, 5)) {
                case 1 -> "Any new Updates?";
                case 2 -> "How to avoid bullying / rules applied / avoid racism";
                case 3 -> "What is an AI Model?";
                case 4 -> "How to protect my data?";
                default -> "How to recover account?";
            };
        }
    }

    public static class MultipleTasksHandler {
        public static ArrayBlockingQueue<Thread> runnables = new ArrayBlockingQueue<>(10);
        public static ExecutorService executorService = Executors.newFixedThreadPool(5);
        public static Lock mainLock = new ReentrantLock();
        public static Lock animationLock = new ReentrantLock();

        public static ExecutorService getExecutorService() {
            return executorService;
        }

        public static Lock getMainLock() {
            return mainLock;
        }

        public static Lock getAnimationLock() {
            return animationLock;
        }

        public static ArrayBlockingQueue<Thread> getRunnables() {
            return runnables;
        }
    }
}
