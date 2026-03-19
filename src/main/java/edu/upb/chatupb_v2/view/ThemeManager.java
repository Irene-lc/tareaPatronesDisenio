package edu.upb.chatupb_v2.view;

import java.awt.*;

public class ThemeManager {

    public enum Theme {
        UNO,
        DOS,
        TRES,
        CUATRO,
        CINCO
    }

    private static Theme currentTheme = Theme.UNO;
    private static ThemeColors colors = new ThemeColors();

    public static class ThemeColors {
        public Color BG_DARK;
        public Color BG_PANEL;
        public Color BG_SIDEBAR;
        public Color BG_INPUT;

        public Color BG_BUBBLE_ME;
        public Color BG_BUBBLE_YOU;

        public Color ACCENT;
        public Color ACCENT_HOVER;
        public Color TEXT_PRIMARY;
        public Color TEXT_MUTED;
        public Color DIVIDER;

        public Color BTN_SEND;
        public Color BTN_SEND_HOVER;
        public Color BTN_UNIQUE_ACTIVE;

        public Color SYSTEM_RED;
        public Color ONLINE_DOT;
        public Color OFFLINE_DOT;
        public Color NOTIF_DOT;

        public Color BG_NORMAL;
        public Color BG_HOVER;
        public Color BG_SELECTED;

        public Color[] AVATAR_COLORS;

        public Color PIN_BACKGROUND;
        public Color PIN_BORDER;

        public Color UNIQUE_BG;
        public Color UNIQUE_BORDER;
        public Color UNIQUE_TEXT;
    }

    public static void setTheme(Theme theme) {
        currentTheme = theme;
        applyTheme(theme);
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static ThemeColors getColors() {
        return colors;
    }

    public static String getThemeName() {
        switch (currentTheme) {
            case UNO: return "1";
            case DOS: return "2";
            case TRES: return "3";
            case CUATRO: return "4";
            case CINCO: return "5";
            default: return "Unknown";
        }
    }

    public static Theme[] getAllThemes() {
        return Theme.values();
    }

    private static void applyTheme(Theme theme) {
        switch (theme) {
            case UNO:
                applyTheme1();
                break;
            case DOS:
                applyTheme2();
                break;
            case TRES:
                applyTheme3();
                break;
            case CUATRO:
                applyTheme4();
                break;
            case CINCO:
                applyTheme5();
                break;
        }
    }
    private static void applyTheme1() {
        colors.BG_DARK = new Color(15, 20, 35);
        colors.BG_PANEL = new Color(22, 28, 48);
        colors.BG_SIDEBAR = new Color(18, 24, 42);
        colors.BG_INPUT = new Color(30, 38, 62);

        colors.BG_BUBBLE_ME = new Color(79, 70, 229);
        colors.BG_BUBBLE_YOU = new Color(36, 44, 70);

        colors.ACCENT = new Color(129, 140, 248);
        colors.ACCENT_HOVER = new Color(165, 180, 252);
        colors.TEXT_PRIMARY = new Color(240, 242, 255);
        colors.TEXT_MUTED = new Color(148, 163, 184);
        colors.DIVIDER = new Color(255, 255, 255, 18);

        colors.BTN_SEND = new Color(99, 102, 241);
        colors.BTN_SEND_HOVER = new Color(129, 140, 248);
        colors.BTN_UNIQUE_ACTIVE = new Color(251, 191, 36);

        colors.SYSTEM_RED = new Color(248, 113, 113);
        colors.ONLINE_DOT = new Color(52, 211, 153);
        colors.OFFLINE_DOT = new Color(100, 116, 139);
        colors.NOTIF_DOT = new Color(20, 184, 166);

        colors.BG_NORMAL = new Color(18, 24, 42);
        colors.BG_HOVER = new Color(28, 36, 60);
        colors.BG_SELECTED = new Color(79, 70, 229, 55);

        colors.AVATAR_COLORS = new Color[] {
                new Color(99, 102, 241),
                new Color(168, 85, 247),
                new Color(236, 72, 153),
                new Color(20, 184, 166),
                new Color(245, 158, 11),
                new Color(59, 130, 246)
        };

        colors.PIN_BACKGROUND = new Color(45, 38, 90);
        colors.PIN_BORDER = new Color(79, 70, 229, 80);

        colors.UNIQUE_BG = new Color(120, 90, 40);
        colors.UNIQUE_BORDER = new Color(251, 191, 36, 150);
        colors.UNIQUE_TEXT = new Color(254, 243, 199);
    }

    private static void applyTheme2() {
        colors.BG_DARK = new Color(12, 25, 20);
        colors.BG_PANEL = new Color(18, 35, 28);
        colors.BG_SIDEBAR = new Color(15, 30, 24);
        colors.BG_INPUT = new Color(25, 45, 36);

        colors.BG_BUBBLE_ME = new Color(34, 139, 90);
        colors.BG_BUBBLE_YOU = new Color(30, 50, 42);

        colors.ACCENT = new Color(74, 222, 128);
        colors.ACCENT_HOVER = new Color(134, 239, 172);
        colors.TEXT_PRIMARY = new Color(236, 253, 245);
        colors.TEXT_MUTED = new Color(134, 179, 158);
        colors.DIVIDER = new Color(255, 255, 255, 15);

        colors.BTN_SEND = new Color(22, 163, 74);
        colors.BTN_SEND_HOVER = new Color(34, 197, 94);
        colors.BTN_UNIQUE_ACTIVE = new Color(251, 191, 36);

        colors.SYSTEM_RED = new Color(248, 113, 113);
        colors.ONLINE_DOT = new Color(74, 222, 128);
        colors.OFFLINE_DOT = new Color(100, 130, 115);
        colors.NOTIF_DOT = new Color(52, 211, 153);

        colors.BG_NORMAL = new Color(15, 30, 24);
        colors.BG_HOVER = new Color(25, 45, 36);
        colors.BG_SELECTED = new Color(34, 139, 90, 55);

        colors.AVATAR_COLORS = new Color[] {
                new Color(34, 139, 90),
                new Color(22, 163, 74),
                new Color(74, 222, 128),
                new Color(16, 185, 129),
                new Color(5, 150, 105),
                new Color(52, 211, 153)
        };

        colors.PIN_BACKGROUND = new Color(25, 55, 42);
        colors.PIN_BORDER = new Color(34, 139, 90, 80);

        colors.UNIQUE_BG = new Color(60, 100, 50);
        colors.UNIQUE_BORDER = new Color(134, 239, 172, 150);
        colors.UNIQUE_TEXT = new Color(220, 252, 231);
    }

    private static void applyTheme3() {
        colors.BG_DARK = new Color(30, 18, 15);
        colors.BG_PANEL = new Color(42, 26, 22);
        colors.BG_SIDEBAR = new Color(36, 22, 18);
        colors.BG_INPUT = new Color(55, 35, 28);

        colors.BG_BUBBLE_ME = new Color(234, 88, 12);
        colors.BG_BUBBLE_YOU = new Color(60, 40, 35);

        colors.ACCENT = new Color(251, 146, 60);
        colors.ACCENT_HOVER = new Color(253, 186, 116);
        colors.TEXT_PRIMARY = new Color(255, 247, 237);
        colors.TEXT_MUTED = new Color(194, 158, 140);
        colors.DIVIDER = new Color(255, 255, 255, 15);

        colors.BTN_SEND = new Color(234, 88, 12);
        colors.BTN_SEND_HOVER = new Color(249, 115, 22);
        colors.BTN_UNIQUE_ACTIVE = new Color(251, 191, 36);

        colors.SYSTEM_RED = new Color(248, 113, 113);
        colors.ONLINE_DOT = new Color(74, 222, 128);
        colors.OFFLINE_DOT = new Color(140, 115, 105);
        colors.NOTIF_DOT = new Color(251, 146, 60);

        colors.BG_NORMAL = new Color(36, 22, 18);
        colors.BG_HOVER = new Color(50, 32, 26);
        colors.BG_SELECTED = new Color(234, 88, 12, 50);

        colors.AVATAR_COLORS = new Color[] {
                new Color(234, 88, 12),
                new Color(220, 38, 38),
                new Color(249, 115, 22),
                new Color(245, 158, 11),
                new Color(239, 68, 68),
                new Color(251, 146, 60)
        };

        colors.PIN_BACKGROUND = new Color(60, 35, 25);
        colors.PIN_BORDER = new Color(234, 88, 12, 80);

        colors.UNIQUE_BG = new Color(100, 55, 30);
        colors.UNIQUE_BORDER = new Color(253, 186, 116, 150);
        colors.UNIQUE_TEXT = new Color(255, 237, 213);
    }

    private static void applyTheme4() {
        colors.BG_DARK = new Color(12, 22, 28);
        colors.BG_PANEL = new Color(18, 32, 42);
        colors.BG_SIDEBAR = new Color(15, 27, 35);
        colors.BG_INPUT = new Color(25, 42, 55);

        colors.BG_BUBBLE_ME = new Color(6, 148, 162);
        colors.BG_BUBBLE_YOU = new Color(28, 45, 58);

        colors.ACCENT = new Color(34, 211, 238);
        colors.ACCENT_HOVER = new Color(103, 232, 249);
        colors.TEXT_PRIMARY = new Color(236, 254, 255);
        colors.TEXT_MUTED = new Color(125, 175, 190);
        colors.DIVIDER = new Color(255, 255, 255, 15);

        colors.BTN_SEND = new Color(8, 145, 178);
        colors.BTN_SEND_HOVER = new Color(6, 182, 212);
        colors.BTN_UNIQUE_ACTIVE = new Color(251, 191, 36);

        colors.SYSTEM_RED = new Color(248, 113, 113);
        colors.ONLINE_DOT = new Color(45, 212, 191);
        colors.OFFLINE_DOT = new Color(100, 130, 145);
        colors.NOTIF_DOT = new Color(34, 211, 238);
        colors.BG_NORMAL = new Color(15, 27, 35);
        colors.BG_HOVER = new Color(22, 40, 52);
        colors.BG_SELECTED = new Color(6, 148, 162, 50);

        colors.AVATAR_COLORS = new Color[] {
                new Color(6, 148, 162),
                new Color(8, 145, 178),
                new Color(14, 116, 144),
                new Color(34, 211, 238),
                new Color(20, 184, 166),
                new Color(45, 212, 191)
        };

        colors.PIN_BACKGROUND = new Color(20, 45, 58);
        colors.PIN_BORDER = new Color(6, 148, 162, 80);

        colors.UNIQUE_BG = new Color(30, 70, 85);
        colors.UNIQUE_BORDER = new Color(103, 232, 249, 150);
        colors.UNIQUE_TEXT = new Color(207, 250, 254);
    }

    private static void applyTheme5() {
        colors.BG_DARK = new Color(28, 15, 25);
        colors.BG_PANEL = new Color(40, 22, 35);
        colors.BG_SIDEBAR = new Color(34, 18, 30);
        colors.BG_INPUT = new Color(55, 30, 48);

        colors.BG_BUBBLE_ME = new Color(190, 24, 93);
        colors.BG_BUBBLE_YOU = new Color(55, 35, 48);

        colors.ACCENT = new Color(244, 114, 182);
        colors.ACCENT_HOVER = new Color(249, 168, 212);
        colors.TEXT_PRIMARY = new Color(253, 242, 248);
        colors.TEXT_MUTED = new Color(180, 140, 165);
        colors.DIVIDER = new Color(255, 255, 255, 15);

        colors.BTN_SEND = new Color(219, 39, 119);
        colors.BTN_SEND_HOVER = new Color(236, 72, 153);
        colors.BTN_UNIQUE_ACTIVE = new Color(251, 191, 36);

        colors.SYSTEM_RED = new Color(248, 113, 113);
        colors.ONLINE_DOT = new Color(134, 239, 172);
        colors.OFFLINE_DOT = new Color(130, 105, 120);
        colors.NOTIF_DOT = new Color(244, 114, 182);

        colors.BG_NORMAL = new Color(34, 18, 30);
        colors.BG_HOVER = new Color(48, 28, 42);
        colors.BG_SELECTED = new Color(190, 24, 93, 50);

        colors.AVATAR_COLORS = new Color[] {
                new Color(219, 39, 119),
                new Color(190, 24, 93),
                new Color(236, 72, 153),
                new Color(168, 85, 247),
                new Color(244, 114, 182),
                new Color(232, 121, 249)
        };

        colors.PIN_BACKGROUND = new Color(55, 28, 48);
        colors.PIN_BORDER = new Color(190, 24, 93, 80);

        colors.UNIQUE_BG = new Color(90, 40, 70);
        colors.UNIQUE_BORDER = new Color(249, 168, 212, 150);
        colors.UNIQUE_TEXT = new Color(252, 231, 243);
    }

    static {
        applyTheme1();
    }
}
