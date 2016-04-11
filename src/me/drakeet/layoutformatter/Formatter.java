package me.drakeet.layoutformatter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 16/4/10 15:01
 */
public class Formatter {

    private static int priority = 0;
    private static final int MAX_PRIORITY = Integer.MAX_VALUE / 2;
    private static int maxPriority = MAX_PRIORITY;
    private static HashMap<String, Integer> sRule = new HashMap<String, Integer>();


    public static int getPriority() {
        return priority;
    }


    public static int getNextPriority() {
        return ++priority;
    }


    public static int getMaxNextPriority() {
        return ++maxPriority;
    }


    static {
        sRule.put("xmlns:android", getNextPriority());
        sRule.put("xmlns:app", getNextPriority());
        sRule.put("xmlns:tools", getNextPriority());
        sRule.put("style", getNextPriority());
        sRule.put(android("id"), getNextPriority());
        sRule.put(android("layout_width"), getNextPriority());
        sRule.put(android("layout_height"), getNextPriority());
        sRule.put(android("orientation"), getNextPriority());
        sRule.put(android("weightSum"), getNextPriority());
        sRule.put(android("layout_weight"), getNextPriority());
        sRule.put(android("layout_gravity"), getNextPriority());
        sRule.put(android("gravity"), getNextPriority());
        sRule.put(android("layout_marginLeft"), getNextPriority());
        sRule.put(android("layout_marginStart"), getPriority());
        sRule.put(android("layout_marginTop"), getNextPriority());
        sRule.put(android("layout_marginRight"), getNextPriority());
        sRule.put(android("layout_marginEnd"), getPriority());
        sRule.put(android("layout_marginBottom"), getNextPriority());

        sRule.put(android("paddingLeft"), getNextPriority());
        sRule.put(android("paddingStart"), getPriority());
        sRule.put(android("paddingTop"), getNextPriority());
        sRule.put(android("paddingRight"), getNextPriority());
        sRule.put(android("paddingEnd"), getPriority());
        sRule.put(android("paddingBottom"), getNextPriority());
        sRule.put(android("background"), getNextPriority());
        sRule.put(android("foreground"), getNextPriority());
        sRule.put(android("clickable"), getNextPriority());
        sRule.put(android("onClick"), getNextPriority());
        sRule.put(android("scaleType"), getNextPriority());
        sRule.put(android("inputType"), getNextPriority());
        sRule.put(android("autoLink"), getNextPriority());
        sRule.put(android("translationZ"), getNextPriority());
        sRule.put(android("elevation"), getNextPriority());

        sRule.put(android("alpha"), getMaxNextPriority());
        sRule.put(android("textColorHint"), getMaxNextPriority());
        sRule.put(android("textColor"), getMaxNextPriority());
        sRule.put(android("hint"), getMaxNextPriority());
        sRule.put(tools("hint"), getMaxNextPriority());
        sRule.put(android("text"), getMaxNextPriority());
        sRule.put(tools("text"), getMaxNextPriority());
        sRule.put(android("src"), getMaxNextPriority());
        sRule.put(tools("src"), getMaxNextPriority());
    }


    private static String android(String name) {
        return String.format("android:%s", name);
    }


    private static String app(String name) {
        return String.format("app:%s", name);
    }


    private static String tools(String name) {
        return String.format("tools:%s", name);
    }


    private static int getLinePriority(String line) {
        try {
            return sRule.get(line.trim().split("=")[0]);
        } catch (NullPointerException e) {
            return MAX_PRIORITY;
        }
    }


    // TODO: 16/4/11 Need to support the multi-lines strings
    public static String apply(String xml) {
        String[] _lines = xml.split(System.getProperty("line.separator"));
        List<String> lines = Arrays.asList(_lines);
        int start = 0, end;
        boolean skip = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.startsWith("</") && line.startsWith("<")) {
                start = i;
            } else if (line.startsWith("</")) {
                skip = true;
            } else if (line.endsWith(">")) {
                if (!skip) {
                    end = i;
                    reformat(lines, start + 1, end);
                } else {
                    skip = false;
                }
            }
        }
        String result = "";
        for (String line : lines) {
            System.out.println(line);
            result += line + "\n";
        }

        return result;
    }


    private static void reformat(List<String> lines, int start, int end) {
        if (start >= lines.size() || start > end || end >= lines.size()) {
            return;
        }
        int i, j;
        String temp;
        for (i = start; i < end - 1; i++) {
            for (j = start; j < end - 1 - i + start; j++) {
                if (getLinePriority(lines.get(j)) > getLinePriority(lines.get(j + 1))) {
                    temp = lines.get(j);
                    lines.set(j, lines.get(j + 1));
                    lines.set(j + 1, temp);
                }
            }
        }
    }
}