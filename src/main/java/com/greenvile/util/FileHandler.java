/* seems big scary and booga wooga black magic go but its simple af
A bunch of renamed edited coppuy pasted lines
its just a class that handles the text writing to file
If you are confused by this class let me know, Ill make sure to explain it to you  */
package com.greenvile.util;

import com.greenvile.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private String dataFilePath = "data/greenvile_data.txt";
    private String websiteDataPath = "website/data/website_data.json";

    public void saveData(DataManager data) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(dataFilePath));
            
            writer.println("COMMUNAL_POOL");
            writer.println(data.getCommunalPointsPool());
            writer.println("END_COMMUNAL_POOL");
            
            writer.println("RESIDENTS");
            for (Resident r : data.getResidents()) {
                writer.println(r.getId() + "|" + r.getFullName() + "|" + r.getPhoneNumber() + "|" + 
                    r.getEmail() + "|" + r.getAddress() + "|" + r.getPersonalPoints() + "|" + r.getLastTaskCompletionDate());
            }
            writer.println("END_RESIDENTS");
            
            writer.println("GREEN_ACTIONS");
            for (GreenAction a : data.getGreenActions()) {
                writer.println(a.getId() + "|" + a.getTitle() + "|" + a.getDescription() + "|" + 
                    a.getPicturePath() + "|" + a.getPointsAwarded() + "|" + a.isDisplayOnWebsite() + "|" + a.getParticipantId());
            }
            writer.println("END_GREEN_ACTIONS");
            
            writer.println("DEFAULT_TASKS");
            for (CommunalTask t : data.getDefaultTasks()) {
                writer.println(t.getId() + "|" + t.getTitle() + "|" + t.getDescription() + "|" + t.getPointsAwarded());
            }
            writer.println("END_DEFAULT_TASKS");
            
            writer.println("COMMUNAL_GOALS");
            for (CommunalGoal g : data.getCommunalGoals()) {
                writer.println("GOAL|" + g.getId() + "|" + g.getTitle() + "|" + g.getDescription() + "|" + 
                    g.getPointsNeeded() + "|" + g.getCurrentPoints() + "|" + g.isCompleted() + "|" + g.isActive());
                for (CommunalTask t : g.getTasks()) {
                    String participants = "";
                    for (int i = 0; i < t.getParticipantIds().size(); i++) {
                        participants += t.getParticipantIds().get(i);
                        if (i < t.getParticipantIds().size() - 1) {
                            participants += ",";
                        }
                    }
                    writer.println("TASK|" + t.getId() + "|" + t.getTitle() + "|" + t.getDescription() + "|" + 
                        t.getPointsAwarded() + "|" + t.isCompleted() + "|" + t.isDisplayOnWebsite() + "|" + participants);
                }
            }
            writer.println("END_COMMUNAL_GOALS");
            
            writer.println("TRADES");
            for (Trade t : data.getTrades()) {
                writer.println(t.getId() + "|" + t.getTitle() + "|" + t.getDescription() + "|" + 
                    t.getPicturePath() + "|" + t.getPointsCost() + "|" + t.getResidentId() + "|" + 
                    t.isCompleted() + "|" + t.isDisplayOnWebsite() + "|" + t.getBuyerId());
            }
            writer.println("END_TRADES");
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private int parseIntSafe(String s) {
        if (s == null || s.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean parseBoolSafe(String s) {
        if (s == null || s.trim().isEmpty()) {
            return false;
        }
        return Boolean.parseBoolean(s.trim());
    }

    public DataManager loadData() {
        DataManager data = new DataManager();
        File file = new File(dataFilePath);
        
        if (!file.exists()) {
            return data;
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String section = "";
            CommunalGoal currentGoal = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.equals("COMMUNAL_POOL")) {
                    section = "COMMUNAL_POOL";
                    String poolLine = reader.readLine();
                    int pool = parseIntSafe(poolLine);
                    data.setCommunalPointsPool(pool);
                } else if (line.equals("RESIDENTS")) {
                    section = "RESIDENTS";
                } else if (line.equals("GREEN_ACTIONS")) {
                    section = "GREEN_ACTIONS";
                } else if (line.equals("DEFAULT_TASKS")) {
                    section = "DEFAULT_TASKS";
                } else if (line.equals("COMMUNAL_GOALS")) {
                    section = "COMMUNAL_GOALS";
                } else if (line.equals("TRADES")) {
                    section = "TRADES";
                } else if (line.startsWith("END_")) {
                    section = "";
                    currentGoal = null;
                } else if (section.equals("RESIDENTS") && !line.isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 6) {
                        Resident r = new Resident(
                            parseIntSafe(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4],
                            parseIntSafe(parts[5])
                        );
                        if (parts.length > 6) {
                            r.setLastTaskCompletionDate(parts[6]);
                        }
                        data.getResidents().add(r);
                    }
                } else if (section.equals("GREEN_ACTIONS") && !line.isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 5) {
                        GreenAction a = new GreenAction(
                            parseIntSafe(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            parseIntSafe(parts[4])
                        );
                        if (parts.length > 5) {
                            a.setDisplayOnWebsite(parseBoolSafe(parts[5]));
                        }
                        if (parts.length > 6 && !parts[6].isEmpty()) {
                            a.setParticipantId(parseIntSafe(parts[6]));
                        }
                        data.getGreenActions().add(a);
                    }
                } else if (section.equals("DEFAULT_TASKS") && !line.isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 4) {
                        CommunalTask t = new CommunalTask(
                            parseIntSafe(parts[0]),
                            parts[1],
                            parts[2],
                            parseIntSafe(parts[3])
                        );
                        data.getDefaultTasks().add(t);
                    }
                } else if (section.equals("COMMUNAL_GOALS") && !line.isEmpty()) {
                    if (line.startsWith("GOAL|")) {
                        String[] parts = line.substring(5).split("\\|", -1);
                        if (parts.length >= 4) {
                            currentGoal = new CommunalGoal(
                                parseIntSafe(parts[0]),
                                parts[1],
                                parts[2],
                                parseIntSafe(parts[3])
                            );
                            if (parts.length > 4) {
                                currentGoal.setCurrentPoints(parseIntSafe(parts[4]));
                            }
                            if (parts.length > 5) {
                                currentGoal.setCompleted(parseBoolSafe(parts[5]));
                            }
                            if (parts.length > 6) {
                                currentGoal.setActive(parseBoolSafe(parts[6]));
                            }
                            data.getCommunalGoals().add(currentGoal);
                        }
                    } else if (line.startsWith("TASK|") && currentGoal != null) {
                        String[] parts = line.substring(5).split("\\|", -1);
                        if (parts.length >= 4) {
                            CommunalTask t = new CommunalTask(
                                parseIntSafe(parts[0]),
                                parts[1],
                                parts[2],
                                parseIntSafe(parts[3])
                            );
                            if (parts.length > 4) {
                                t.setCompleted(parseBoolSafe(parts[4]));
                            }
                            if (parts.length > 5) {
                                t.setDisplayOnWebsite(parseBoolSafe(parts[5]));
                            }
                            if (parts.length > 6 && !parts[6].isEmpty()) {
                                String[] pIds = parts[6].split(",");
                                for (String pid : pIds) {
                                    if (!pid.trim().isEmpty()) {
                                        t.addParticipant(parseIntSafe(pid));
                                    }
                                }
                            }
                            currentGoal.addTask(t);
                        }
                    }
                } else if (section.equals("TRADES") && !line.isEmpty()) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 6) {
                        Trade t = new Trade(
                            parseIntSafe(parts[0]),
                            parts[1],
                            parts[2],
                            parts[3],
                            parseIntSafe(parts[4]),
                            parseIntSafe(parts[5])
                        );
                        if (parts.length > 6) {
                            t.setCompleted(parseBoolSafe(parts[6]));
                        }
                        if (parts.length > 7) {
                            t.setDisplayOnWebsite(parseBoolSafe(parts[7]));
                        }
                        if (parts.length > 8) {
                            t.setBuyerId(parseIntSafe(parts[8]));
                        }
                        data.getTrades().add(t);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        
        return data;
    }

    public void exportToWebsite(DataManager data) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(websiteDataPath));
            
            writer.println("{");
            
            writer.println("  \"greenActions\": [");
            List<GreenAction> displayActions = new ArrayList<>();
            for (GreenAction a : data.getGreenActions()) {
                if (a.isDisplayOnWebsite()) {
                    displayActions.add(a);
                }
            }
            for (int i = 0; i < displayActions.size(); i++) {
                GreenAction a = displayActions.get(i);
                writer.println("    {");
                writer.println("      \"title\": \"" + escapeJson(a.getTitle()) + "\",");
                writer.println("      \"description\": \"" + escapeJson(a.getDescription()) + "\",");
                writer.println("      \"picture\": \"" + escapeJson(a.getPicturePath()) + "\"");
                writer.print("    }");
                if (i < displayActions.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            writer.println("  ],");
            
            writer.println("  \"communalGoals\": [");
            List<CommunalGoal> activeGoals = new ArrayList<>();
            for (CommunalGoal g : data.getCommunalGoals()) {
                if (g.isActive() || g.isCompleted()) {
                    activeGoals.add(g);
                }
            }
            for (int i = 0; i < activeGoals.size(); i++) {
                CommunalGoal g = activeGoals.get(i);
                writer.println("    {");
                writer.println("      \"title\": \"" + escapeJson(g.getTitle()) + "\",");
                writer.println("      \"description\": \"" + escapeJson(g.getDescription()) + "\",");
                writer.println("      \"pointsNeeded\": " + g.getPointsNeeded() + ",");
                writer.println("      \"currentPoints\": " + g.getCurrentPoints() + ",");
                writer.println("      \"completed\": " + g.isCompleted() + ",");
                writer.println("      \"tasks\": [");
                List<CommunalTask> displayTasks = new ArrayList<>();
                for (CommunalTask t : g.getTasks()) {
                    if (t.isDisplayOnWebsite()) {
                        displayTasks.add(t);
                    }
                }
                for (int j = 0; j < displayTasks.size(); j++) {
                    CommunalTask t = displayTasks.get(j);
                    writer.println("        {");
                    writer.println("          \"title\": \"" + escapeJson(t.getTitle()) + "\",");
                    writer.println("          \"description\": \"" + escapeJson(t.getDescription()) + "\",");
                    writer.println("          \"completed\": " + t.isCompleted());
                    writer.print("        }");
                    if (j < displayTasks.size() - 1) {
                        writer.println(",");
                    } else {
                        writer.println();
                    }
                }
                writer.println("      ]");
                writer.print("    }");
                if (i < activeGoals.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            writer.println("  ],");
            
            writer.println("  \"trades\": [");
            List<Trade> displayTrades = new ArrayList<>();
            for (Trade t : data.getTrades()) {
                if (t.isDisplayOnWebsite() && !t.isCompleted()) {
                    displayTrades.add(t);
                }
            }
            for (int i = 0; i < displayTrades.size(); i++) {
                Trade t = displayTrades.get(i);
                writer.println("    {");
                writer.println("      \"title\": \"" + escapeJson(t.getTitle()) + "\",");
                writer.println("      \"description\": \"" + escapeJson(t.getDescription()) + "\",");
                writer.println("      \"picture\": \"" + escapeJson(t.getPicturePath()) + "\",");
                writer.println("      \"pointsCost\": " + t.getPointsCost());
                writer.print("    }");
                if (i < displayTrades.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            writer.println("  ],");
            
            writer.println("  \"communalPointsPool\": " + data.getCommunalPointsPool());
            
            writer.println("}");
            
            writer.close();
        } catch (IOException e) {
            System.out.println("Error exporting to website: " + e.getMessage());
        }
    }

    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    public boolean fileExists() {
        File file = new File(dataFilePath);
        return file.exists();
    }
}
