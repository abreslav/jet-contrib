package org.jetbrains.demo.ukhorskaya.examplesLoader;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.demo.ukhorskaya.ErrorWriter;
import org.jetbrains.demo.ukhorskaya.ErrorWriterOnServer;
import org.jetbrains.demo.ukhorskaya.server.ServerSettings;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Natalia.Ukhorskaya
 * Date: 11/16/11
 * Time: 6:33 PM
 */
public class ExamplesList {
    //    private static final Logger LOG = Logger.getLogger(ExamplesList.class);
    private static final ExamplesList EXAMPLES_LIST = new ExamplesList();

    private static StringBuilder response;

    private ExamplesList() {
        response = new StringBuilder();
        list = new ArrayList<Map<String, String>>();
        generateList();
    }

    private static List<Map<String, String>> list;

    public static ExamplesList getInstance() {
        return EXAMPLES_LIST;
    }

    public List<Map<String, String>> getList() {
        return list;
    }

    @Nullable
    public Pair<Integer, String> findExampleByName(String name) {
        int i = 0;
        String lastHead = "";
        name = name.replaceAll("_", " ");
        for (Map<String, String> map : list) {
            if (map.get("type").equals("folder")) {
                lastHead = map.get("text");
            } else if (map.get("type").equals("content")) {
                if (map.get("text").equals(name)) {
                    return new Pair<Integer, String>(i, lastHead);
                }
            }
            i++;
        }
        return null;
    }


    @Nullable
    public Pair<Integer, String> findExampleByNameAndHead(String name, String head) {
        int i = 0;
        String lastHead = "";
        name = name.replaceAll("_", " ");
        head = head.replaceAll("_", " ");
        for (Map<String, String> map : list) {
            if (map.get("type").equals("folder")) {
                lastHead = map.get("text");
            } else if (map.get("type").equals("content")) {
                if (map.get("text").equals(name)) {
                    if (lastHead.equals(head)) {
                        return new Pair<Integer, String>(i, lastHead);
                    }
                }
            }
            i++;
        }
        return null;
    }

    public Map<String, String> getMapFromList(int id) {
        if (id < list.size()) {
            return list.get(id);
        }

        ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error("There is a request for example with number " + id + " - absent in map");
        return list.get(1);
    }

    private void generateList() {
        File root = new File(ServerSettings.EXAMPLES_ROOT);
        if (root.exists()) {
            File order = checkIsOrderTxtExists(root);
            if (order != null) {
                addInOrder(order, root, true);
            } else {
                addWoOrder(root, true);
            }
        } else {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error("Examples root doesn't exists");
            ErrorWriter.writeErrorToConsole("Examples root doesn't exists");
            response.append("\nExamples root doesn't exists");
        }
        ErrorWriter.writeInfoToConsole("Examples were loaded.");
        response.append("\nExamples were loaded.");
    }

    private void addWoOrder(File parent, boolean isDirectory) {
        File[] children = parent.listFiles();
        for (File child : children) {
            if ((parent.isDirectory() && isDirectory)
                    || (parent.exists() && !isDirectory)) {
                Map<String, String> map = new HashMap<String, String>();
                if (isDirectory) {
                    map.put("type", "folder");
                    map.put("text", child.getName());
                } else {
                    map.put("type", "content");
                    if (child.getName().endsWith(".kt")) {
                        map.put("text", child.getName().substring(0, child.getName().length() - 3));
                    } else {
                        map.put("text", child.getName());
                    }

                }

                list.add(map);

                if (isDirectory) {
                    File order = new File(child.getAbsolutePath() + File.separator + "order.txt");
                    if (order.exists()) {
                        addInOrder(order, child, false);
                    } else {
                        addWoOrder(child, false);
                    }
                }
            } else {
                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error("Incorrect structure for examples (folder - files): " + child.getAbsolutePath());
            }
        }
    }

    private void addInOrder(File order, File parent, boolean isDirectory) {
        try {
            String[] children = parent.list();
            FileReader fReader = new FileReader(order);
            BufferedReader reader = new BufferedReader(fReader);
            String tmp = "";
            List<String> orderedChildren = new ArrayList<String>();
            while ((tmp = reader.readLine()) != null) {
                File child = new File(parent.getAbsolutePath() + File.separator + tmp);
                if ((child.isDirectory() && isDirectory)
                        || (child.exists() && !isDirectory)) {
                    Map<String, String> map = new HashMap<String, String>();
                    if (isDirectory) {
                        map.put("type", "folder");
                        map.put("text", child.getName());
                    } else {
                        map.put("type", "content");
                        if (child.getName().endsWith(".kt")) {
                            map.put("text", child.getName().substring(0, child.getName().length() - 3));
                        } else {
                            map.put("text", child.getName());
                        }
                    }
                    list.add(map);
                    orderedChildren.add(child.getName());

                    if (isDirectory) {
                        File orderChildren = checkIsOrderTxtExists(child);
                        if (orderChildren != null) {
                            addInOrder(orderChildren, child, false);
                        } else {
                            addWoOrder(child, false);
                        }
                    }
                }
            }
            //+1 for order.txt
            if (orderedChildren.size() + 1 < children.length) {
                for (String childName : children) {
                    if (!childName.equals("order.txt") && !childName.equals("helpExamples.xml")) {
                        boolean isAdded = false;
                        for (String orderedChild : orderedChildren) {
                            if (childName.equals(orderedChild)) {
                                isAdded = true;
                            }
                        }
                        if (!isAdded) {
                            File child = new File(parent.getAbsolutePath() + File.separator + childName);
                            if ((child.isDirectory() && isDirectory)
                                    || (child.exists() && !isDirectory)) {
                                Map<String, String> map = new HashMap<String, String>();
                                if (isDirectory) {
                                    map.put("type", "folder");
                                } else {
                                    map.put("type", "content");
                                }
                                map.put("text", child.getName());
                                list.add(map);
                                ErrorWriter.writeErrorToConsole("File/Directory " + childName + " is absent in order.txt and was added at end.");
                                ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error("File/Directory " + childName + " is absent in order.txt and was added at end.");
                                response.append("\nFile/Directory " + childName + " is absent in order.txt and was added at end.");

                                if (isDirectory) {
                                    File orderChildren = checkIsOrderTxtExists(child);
                                    if (orderChildren != null) {
                                        addInOrder(orderChildren, child, false);
                                    } else {
                                        addWoOrder(child, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error("Cannot find order.txt file: " + order.getAbsolutePath(), e);
        } catch (IOException e) {
            ErrorWriterOnServer.LOG_FOR_EXCEPTIONS.error("Cannot read order.txt file: " + order.getAbsolutePath(), e);
        }
    }

    @Nullable
    private File checkIsOrderTxtExists(File root) {
        File order = new File(root.getAbsolutePath() + File.separator + "order.txt");
        if (order.exists()) {
            return order;
        }
        return null;
    }

    public static String updateList() {
        response = new StringBuilder();
        list = new ArrayList<Map<String, String>>();
        ExamplesList.getInstance().generateList();
        return response.toString();
    }
}
