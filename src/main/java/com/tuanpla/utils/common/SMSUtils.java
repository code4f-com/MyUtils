package com.tuanpla.utils.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSUtils {

    private static final String HEXINDEX = "0123456789abcdef          ABCDEF";
    public static String CHAR_PARSE_MT = "###";
    public static String[] SO_NGIEP_VU = {};
    //-------------------------------------
    public static final String VIETTEL_OPERATOR = "VTE";
    public static String VINA_OPERATOR = "GPC";
    public static String MOBI_OPERATOR = "VMS";
    public static String VNM_OPERATOR = "VNM";
    public static String BEELINE_OPERATOR = "BL";
    public static int REJECT_MSG_LENG = -1;

    public static enum OPER {

        VIETTEL("VTE"),
        VINA("GPC"),
        MOBI("VMS"),
        VNM("VNM"),
        BEELINE("BL");
        public String val;

        public String getVal() {
            return val;
        }

        private OPER(String val) {
            this.val = val;
        }
    }

    public static String removePassLog(String input) {
        if (MyString.isEmpty(input)) {
            return "";
        }
        String[] arr = input.split("\\|");
        String tmp = "";
        for (String one : arr) {
            if (!MyString.isEmpty(one)) {
                one = one.trim();
            }
            if (!one.startsWith("pass")) {
                tmp += one + " |";
            }
        }
        if (!MyString.isEmpty(tmp) && tmp.endsWith("|")) {
            tmp = tmp.substring(0, (tmp.length() - 1));
            tmp = tmp.trim();
        }
        return tmp;
    }

    public static boolean validPhoneVN(String phone) {
        String regex = "^(\\+849\\d{8})|(849\\d{8})|(09\\d{8})|(\\+841\\d{9})|(841\\d{9})|(01\\d{9})|(\\+848\\d{8})|(848\\d{8})|(08\\d{8})$";
        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);
        // Now create matcher object.
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isSoNghiepVu(String snumber) {
        boolean flag = false;
        for (String arr1 : SO_NGIEP_VU) {
            if (arr1.endsWith(snumber)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static int countNomalSMS(String msg) {
        int result = 0;
        if (!MyString.isEmpty(msg)) {
            if (msg.length() <= 160) {
                result = 1;
            } else {
                result = msg.length() / 153;
                if (msg.length() % 153 != 0) {
                    result = result + 1;
                }
            }
        }
        return result;
    }

    public static int countSmsBrandQC(String mess, String oper) {
        int count = 1;
        if (!MyString.isEmpty(mess)) {
            int length = mess.length();
            if (oper.equals(VIETTEL_OPERATOR)) {
                if (length <= 123) {
                    count = 1;
                } else if (length > 123 && length <= 268) {
                    count = 2;
                } else if (length > 268 && length <= 421) {
                    count = 3;
                } else if (length > 421 && length <= 574) {
                    count = 4;
                } else {
                    count = REJECT_MSG_LENG;
                }
            } else if (oper.equals(VINA_OPERATOR)) {
                if (length <= 123) {
                    count = 1;
                } else if (length > 123 && length <= 268) {
                    count = 2;
                } else if (length > 268 && length <= 421) {
                    count = 3;
                } else if (length > 421 && length <= 574) {
                    count = 4;
                } else {
                    count = REJECT_MSG_LENG;
                }
            } else if (oper.equals(MOBI_OPERATOR)) {
                if (length <= 127) {
                    count = 1;
                } else if (length > 127 && length <= 273) {
                    count = 2;
                } else if (length > 273 && length <= 426) {
                    count = 3;
                } else if (length > 426 && length <= 579) {
                    count = 4;
                } else {
                    count = REJECT_MSG_LENG;
                }
            } else if (length <= 160) {
                count = 1;
            } else if (length > 160 && length <= 306) {
                count = 2;
            } else if (length > 306 && length <= 459) {
                count = 3;
            } else if (length > 459 && length <= 612) {
                count = 4;
            } else {
                count = REJECT_MSG_LENG;
            }
        } else {
            count = 0;
        }
        return count;
    }

    public static int countSmsBrandCSKH(String mess, String oper) {
        int count = 1;
        if (!MyString.isEmpty(mess)) {
            int length = mess.length();
            if (oper.equals(VIETTEL_OPERATOR)) {
                if (length <= 160) {
                    count = 1;
                } else if (length > 160 && length <= 306) {
                    count = 2;
                } else if (length > 306 && length <= 459) {
                    count = 3;
                } else {
                    count = REJECT_MSG_LENG;
                }
            } else if (oper.equals(VINA_OPERATOR)) {
                if (length <= 160) {
                    count = 1;
                } else if (length > 160 && length <= 306) {
                    count = 2;
                } else if (length > 306 && length <= 459) {
                    count = 3;
                } else if (length > 459 && length <= 612) {
                    count = 4;
                } else {
                    count = REJECT_MSG_LENG;
                }
            } else if (oper.equals(MOBI_OPERATOR)) {
                if (length <= 160) {
                    count = 1;
                } else if (length > 160 && length <= 306) {
                    count = 2;
                } else if (length > 306 && length <= 459) {
                    count = 3;
                } else if (length > 459 && length <= 612) {
                    count = 4;
                } else {
                    count = REJECT_MSG_LENG;
                }
            } else if (length <= 160) {
                count = 1;
            } else if (length > 160 && length <= 306) {
                count = 2;
            } else if (length > 306 && length <= 459) {
                count = 3;
            } else if (length > 459 && length <= 612) {
                count = 4;
            } else {
                count = REJECT_MSG_LENG;
            }
        } else {
            count = 0;
        }
        return count;
    }

    public static String[] arrangeCommandCode(String[] allCommandCode) {
        try {
            for (int i = 0; i < allCommandCode.length; i++) {
                for (int j = i + 1; j < allCommandCode.length; j++) {
                    String stem = "";
                    if (allCommandCode[j].length() > allCommandCode[i].length()) {
                        stem = allCommandCode[i].toUpperCase();
                        allCommandCode[i] = allCommandCode[j].toUpperCase();
                        allCommandCode[j] = stem;
                    }
                }
            }
        } catch (Exception e) {
            return allCommandCode;
        }
        return allCommandCode;
    }

    public static ArrayList validList(String listPhone) {
        ArrayList list = new ArrayList();
        if (listPhone != null) {
            String[] arrPhone = listPhone.split("[,;: ]");
            if (arrPhone != null && arrPhone.length > 0) {
                for (String onePhone : arrPhone) {
                    // Valid 1 Phone
                    if (onePhone == null) {
                        continue;
                    }
                    if (SMSUtils.checkPhoneNumber(onePhone) == 1) {
                        // So dien thoai hop le
                        onePhone = SMSUtils.PhoneTo84(onePhone);
                        list.add(onePhone);
                    }
                }
            }
        }
        return list;
    }

    public static ArrayList validList(ArrayList<String> listPhone) {
        ArrayList list = new ArrayList();
        // VIETTEL AND OTHER
        if (listPhone != null) {
            for (String onePhone : listPhone) {
                // Valid 1 Phone
                if (onePhone == null) {
                    continue;
                }
                if (SMSUtils.checkPhoneNumber(onePhone) == 1) {
                    list.add(onePhone);
                }
            }
        }
        return list;
    }

    public static String validBrand(String input, String oper) {
        String str = input;
        if (input != null) {
            // Tam bo: vi Brand KH dang ky the nao gui sang VMG the chi có VMS là neu DK dấu cách thì KH nhận được sẽ không có dấu cách
//            if (oper.equalsIgnoreCase(MOBI_OPERATOR)) {
//                // Chi co mo bi khong cho dau cach thoi
//                str = input.replaceAll(" ", "");
//            }
        }
        return str;
    }

    public static String PhoneTo84(String number) {
        if (number == null) {
            number = "";
            return number;
        }
        number = number.replaceAll("o", "0");
        if (number.startsWith("84")) {
            return number;
        } else if (number.startsWith("0")) {
            number = "84" + number.substring(1);
        } else if (number.startsWith("+84")) {
            number = number.substring(1);
        } else {
            number = "84" + number;
        }
        return number;
    }

    /**
     * PLA TUAN Loai Bo Cac Ky Tu Dac biet trong Msg
     *
     * @param msg
     * @return
     */
    public static String validMessage(String msg) {
        msg = msg.replace('.', ' ');
        msg = msg.replace('!', ' ');
        msg = msg.replace('$', ' ');
        msg = msg.replace('#', ' ');
        msg = msg.replace('[', ' ');
        msg = msg.replace(']', ' ');
        msg = msg.replace('(', ' ');
        msg = msg.replace(')', ' ');
        msg = msg.replace(',', ' ');
        msg = msg.replace(';', ' ');
        msg = msg.replace('"', ' ');
        msg = msg.replace('\'', ' ');
        msg = msg.replace('\\', ' ');
        msg = msg.replace('/', ' ');
        msg = msg.replace('%', ' ');
        msg = msg.replace('<', ' ');
        msg = msg.replace('>', ' ');
        msg = msg.replace('@', ' ');
        msg = msg.replace(':', ' ');
        msg = msg.replace('=', ' ');
        msg = msg.replace('?', ' ');
        msg = msg.replace('-', ' ');
        msg = msg.replace('_', ' ');
        msg = msg.trim();
        StringTokenizer tk = new StringTokenizer(msg, " ");
        msg = "";
        while (tk.hasMoreTokens()) {
            String sTmp = (String) tk.nextToken();
            if (!msg.equals("")) {
                msg += " " + sTmp;
            } else {
                msg += sTmp;
            }
        }
        msg = MyString.convert2NoSign(msg);
        return msg;
    }

    /**
     *
     * @param phoneNumber
     * @return
     */
    public static String PhoneTo09(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.equals("")) {
            return "";
        }
        try {
            if (phoneNumber.startsWith("84") && phoneNumber.length() > 2) {
                phoneNumber = "0" + phoneNumber.substring(2).toString();
            } else if (phoneNumber.startsWith("+84") && phoneNumber.length() > 3) {
                phoneNumber = "0" + phoneNumber.substring(3).toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return phoneNumber;
    }

    public static String sumNick(String nick) {
        if (nick == null || "".equals(nick)) {
            return null;
        }
        nick = nick.trim();
        int sum = 0;
        if (nick.length() < 2 && isNumberic(nick)) {
            return nick;
        }
        nick = nick.toUpperCase();
        for (int i = 0; i < nick.length(); i++) {
            char ch = nick.charAt(i);
            if (ch == 'A' || ch == 'J' || ch == 'S') {
                sum++;
                continue;
            }
            if (ch == 'B' || ch == 'K' || ch == 'T') {
                sum += 2;
                continue;
            }
            if (ch == 'C' || ch == 'L' || ch == 'U') {
                sum += 3;
                continue;
            }
            if (ch == 'D' || ch == 'M' || ch == 'V') {
                sum += 4;
                continue;
            }
            if (ch == 'E' || ch == 'N' || ch == 'W') {
                sum += 5;
                continue;
            }
            if (ch == 'F' || ch == 'O' || ch == 'X') {
                sum += 6;
                continue;
            }
            if (ch == 'G' || ch == 'P' || ch == 'Y') {
                sum += 7;
                continue;
            }
            if (ch == 'H' || ch == 'Q' || ch == 'Z') {
                sum += 8;
                continue;
            }
            if (ch == 'I' || ch == 'R') {
                sum += 9;
            }
        }

        String sTmp = (new StringBuilder()).append("").append(sum).toString();
        sum = 0;
        int iTmp;
        for (; sTmp.length() != 1; sTmp = String.valueOf(iTmp)) {
            iTmp = 0;
            for (int i = 0; i < sTmp.length(); i++) {
                char temp = sTmp.charAt(i);
                if (Character.isDigit(temp)) {
                    iTmp += Integer.parseInt(String.valueOf(temp));
                }
            }

        }

        return sTmp;
    }

    public static String buildMobileOperator(String userId) {
        String mobileOperator = "OTHER";
        if (MyString.isEmpty(userId)) {
            return mobileOperator;
        }
        if (//-
                userId.startsWith("+8491") || userId.startsWith("8491") || userId.startsWith("091") || userId.startsWith("91")
                || userId.startsWith("+8494") || userId.startsWith("8494") || userId.startsWith("094") || userId.startsWith("94")
                || userId.startsWith("+84123") || userId.startsWith("84123") || userId.startsWith("0123") || userId.startsWith("123")
                || userId.startsWith("+84124") || userId.startsWith("84124") || userId.startsWith("0124") || userId.startsWith("124")
                || userId.startsWith("+84125") || userId.startsWith("84125") || userId.startsWith("0125") || userId.startsWith("125")
                || userId.startsWith("+84127") || userId.startsWith("84127") || userId.startsWith("0127") || userId.startsWith("127")
                || userId.startsWith("+84129") || userId.startsWith("84129") || userId.startsWith("0129") || userId.startsWith("129")
                || userId.startsWith("+8488") || userId.startsWith("8488") || userId.startsWith("088") || userId.startsWith("88") // NEW
                ) {
            //VINA
            mobileOperator = OPER.VINA.val;
        } else if (userId.startsWith("+8490") || userId.startsWith("8490") || userId.startsWith("090") || userId.startsWith("90")
                || userId.startsWith("+8493") || userId.startsWith("8493") || userId.startsWith("093") || userId.startsWith("93")
                || userId.startsWith("+84120") || userId.startsWith("84120") || userId.startsWith("0120") || userId.startsWith("120")
                || userId.startsWith("+84121") || userId.startsWith("84121") || userId.startsWith("0121") || userId.startsWith("121")
                || userId.startsWith("+84122") || userId.startsWith("84122") || userId.startsWith("0122") || userId.startsWith("122")
                || userId.startsWith("+84126") || userId.startsWith("84126") || userId.startsWith("0126") || userId.startsWith("126")
                || userId.startsWith("+84128") || userId.startsWith("84128") || userId.startsWith("0128") || userId.startsWith("128")
                || userId.startsWith("+8489") || userId.startsWith("8489") || userId.startsWith("089") || userId.startsWith("89") // NEW
                ) {
            // MOBILE
            mobileOperator = OPER.MOBI.val;
        } else if (userId.startsWith("+8498") || userId.startsWith("8498") || userId.startsWith("098") || userId.startsWith("98")
                || userId.startsWith("+8497") || userId.startsWith("8497") || userId.startsWith("097") || userId.startsWith("97")
                || userId.startsWith("+84160") || userId.startsWith("84160") || userId.startsWith("0160") || userId.startsWith("160")
                || userId.startsWith("+84161") || userId.startsWith("84161") || userId.startsWith("0161") || userId.startsWith("161")
                || userId.startsWith("+84162") || userId.startsWith("84162") || userId.startsWith("0162") || userId.startsWith("162")
                || userId.startsWith("+84163") || userId.startsWith("84163") || userId.startsWith("0163") || userId.startsWith("163")
                || userId.startsWith("+84164") || userId.startsWith("84164") || userId.startsWith("0164") || userId.startsWith("164")
                || userId.startsWith("+84165") || userId.startsWith("84165") || userId.startsWith("0165") || userId.startsWith("165")
                || userId.startsWith("+84166") || userId.startsWith("84166") || userId.startsWith("0166") || userId.startsWith("166")
                || userId.startsWith("+84167") || userId.startsWith("84167") || userId.startsWith("0167") || userId.startsWith("167")
                || userId.startsWith("+84168") || userId.startsWith("84168") || userId.startsWith("0168") || userId.startsWith("168")
                || userId.startsWith("+84169") || userId.startsWith("84169") || userId.startsWith("0169") || userId.startsWith("169")
                || userId.startsWith("+8486") || userId.startsWith("8486") || userId.startsWith("086") || userId.startsWith("86") // NEW
                // EVN Cu
                || userId.startsWith("96") || userId.startsWith("096") || userId.startsWith("8496") || userId.startsWith("+8496")
                || userId.startsWith("42") || userId.startsWith("042") || userId.startsWith("8442") || userId.startsWith("+8442")) {
            mobileOperator = OPER.VIETTEL.val;
        } else if (userId.startsWith("92") || userId.startsWith("092") || userId.startsWith("8492") || userId.startsWith("+8492")
                || userId.startsWith("188") || userId.startsWith("0188") || userId.startsWith("84188") || userId.startsWith("+84188")) {
            // VIET NAM MOBILE
            mobileOperator = OPER.VNM.val;
        } else if (userId.startsWith("99") || userId.startsWith("099") || userId.startsWith("8499") || userId.startsWith("+8499")
                || userId.startsWith("199") || userId.startsWith("0199") || userId.startsWith("84199") || userId.startsWith("+84199")) {
            mobileOperator = OPER.BEELINE.val;
        } else {
            mobileOperator = "OTHER";
        }
        return mobileOperator;
    }

    public static boolean checkVinaphone(String userId) {
        return userId.startsWith("+8491") || userId.startsWith("8491") || userId.startsWith("091") || userId.startsWith("91")
                || userId.startsWith("+8494") || userId.startsWith("8494") || userId.startsWith("094") || userId.startsWith("94")
                || userId.startsWith("+84123") || userId.startsWith("84123") || userId.startsWith("0123") || userId.startsWith("123")
                || userId.startsWith("+84124") || userId.startsWith("84124") || userId.startsWith("0124") || userId.startsWith("124")
                || userId.startsWith("+84125") || userId.startsWith("84125") || userId.startsWith("0125") || userId.startsWith("125")
                || userId.startsWith("+84127") || userId.startsWith("84127") || userId.startsWith("0127") || userId.startsWith("127")
                || userId.startsWith("+84129") || userId.startsWith("84129") || userId.startsWith("0129") || userId.startsWith("129")
                || userId.startsWith("+8488") || userId.startsWith("8488") || userId.startsWith("088") || userId.startsWith("88") // NEW
                ;
    }

    public static boolean checkMobilePhone(String userId) {
        return userId.startsWith("+8490") || userId.startsWith("8490") || userId.startsWith("090") || userId.startsWith("90")
                || userId.startsWith("+8493") || userId.startsWith("8493") || userId.startsWith("093") || userId.startsWith("93")
                || userId.startsWith("+84120") || userId.startsWith("84120") || userId.startsWith("0120") || userId.startsWith("120")
                || userId.startsWith("+84121") || userId.startsWith("84121") || userId.startsWith("0121") || userId.startsWith("121")
                || userId.startsWith("+84122") || userId.startsWith("84122") || userId.startsWith("0122") || userId.startsWith("122")
                || userId.startsWith("+84126") || userId.startsWith("84126") || userId.startsWith("0126") || userId.startsWith("126")
                || userId.startsWith("+84128") || userId.startsWith("84128") || userId.startsWith("0128") || userId.startsWith("128")
                || userId.startsWith("+8489") || userId.startsWith("8489") || userId.startsWith("089") || userId.startsWith("89") // NEW
                ;
    }

    public static boolean checkVietel(String userId) {
        return userId.startsWith("+8498") || userId.startsWith("8498") || userId.startsWith("098") || userId.startsWith("98")
                || userId.startsWith("+8497") || userId.startsWith("8497") || userId.startsWith("097") || userId.startsWith("97")
                || userId.startsWith("+84160") || userId.startsWith("84160") || userId.startsWith("0160") || userId.startsWith("160")
                || userId.startsWith("+84161") || userId.startsWith("84161") || userId.startsWith("0161") || userId.startsWith("161")
                || userId.startsWith("+84162") || userId.startsWith("84162") || userId.startsWith("0162") || userId.startsWith("162")
                || userId.startsWith("+84163") || userId.startsWith("84163") || userId.startsWith("0163") || userId.startsWith("163")
                || userId.startsWith("+84164") || userId.startsWith("84164") || userId.startsWith("0164") || userId.startsWith("164")
                || userId.startsWith("+84165") || userId.startsWith("84165") || userId.startsWith("0165") || userId.startsWith("165")
                || userId.startsWith("+84166") || userId.startsWith("84166") || userId.startsWith("0166") || userId.startsWith("166")
                || userId.startsWith("+84167") || userId.startsWith("84167") || userId.startsWith("0167") || userId.startsWith("167")
                || userId.startsWith("+84168") || userId.startsWith("84168") || userId.startsWith("0168") || userId.startsWith("168")
                || userId.startsWith("+84169") || userId.startsWith("84169") || userId.startsWith("0169") || userId.startsWith("169")
                || userId.startsWith("+8486") || userId.startsWith("8486") || userId.startsWith("086") || userId.startsWith("86") // NEW
                ;
    }

    public static boolean checkVietnamobile(String userId) {
        return userId.startsWith("92") || userId.startsWith("092") || userId.startsWith("8492") || userId.startsWith("+8492")
                || userId.startsWith("188") || userId.startsWith("0188") || userId.startsWith("84188") || userId.startsWith("+84188");
    }

    public static boolean checkBeeline(String userId) {
        return userId.startsWith("99") || userId.startsWith("099") || userId.startsWith("8499") || userId.startsWith("+8499")
                || userId.startsWith("199") || userId.startsWith("0199") || userId.startsWith("84199") || userId.startsWith("+84199");
    }

    public static int checkPhoneNumber(String userId) {
        userId = userId.replace('o', '0');
        int check = -1;
        try {
            long number = Long.parseLong(userId);
        } catch (NumberFormatException ne) {
            check = -2;      //"Số điện thoại bạn nhập không phải số";
            return check;
        }
        if (userId == null || "".equals(userId)) {
            return 0;   //"Bạn chưa nhập số điện thoại";
        } else if (!((userId.startsWith("88") || userId.startsWith("86") || userId.startsWith("89") || userId.startsWith("90") || userId.startsWith("91") || userId.startsWith("92") || userId.startsWith("93")
                || userId.startsWith("92") || userId.startsWith("99") || userId.startsWith("94") || userId.startsWith("95") || userId.startsWith("96") || userId.startsWith("97") || userId.startsWith("98"))
                && userId.length() == 9)
                && !((userId.startsWith("088") || userId.startsWith("086") || userId.startsWith("089") || userId.startsWith("090") || userId.startsWith("091") || userId.startsWith("092") || userId.startsWith("093")
                || userId.startsWith("092") || userId.startsWith("099") || userId.startsWith("094") || userId.startsWith("095") || userId.startsWith("096") || userId.startsWith("097") || userId.startsWith("098"))
                && userId.length() == 10)
                && !((userId.startsWith("8488") || userId.startsWith("8486") || userId.startsWith("8489") || userId.startsWith("8490") || userId.startsWith("8491") || userId.startsWith("8492") || userId.startsWith("8493")
                || userId.startsWith("8492") || userId.startsWith("8499") || userId.startsWith("8494") || userId.startsWith("8495") || userId.startsWith("8496") || userId.startsWith("8497") || userId.startsWith("8498"))
                && userId.length() == 11)
                && !((userId.startsWith("+8488") || userId.startsWith("+8486") || userId.startsWith("+8489") || userId.startsWith("+8490") || userId.startsWith("+8491") || userId.startsWith("+8492") || userId.startsWith("+8493")
                || userId.startsWith("+8492") || userId.startsWith("+8499") || userId.startsWith("+8494") || userId.startsWith("+8495") || userId.startsWith("+8496") || userId.startsWith("+8497") || userId.startsWith("+8498"))
                && userId.length() == 12)
                && !((userId.startsWith("0160") || userId.startsWith("0161") || userId.startsWith("0162") || userId.startsWith("0163") || userId.startsWith("0164")
                || userId.startsWith("188") || userId.startsWith("0199") || userId.startsWith("0165") || userId.startsWith("0166") || userId.startsWith("0167") || userId.startsWith("0168") || userId.startsWith("0169")
                || userId.startsWith("0120") || userId.startsWith("0121") || userId.startsWith("0122") || userId.startsWith("0123")
                || userId.startsWith("0124") || userId.startsWith("0125") || userId.startsWith("0126")
                || userId.startsWith("0127") || userId.startsWith("0128") || userId.startsWith("0129")) && userId.length() == 11)
                && !((userId.startsWith("84160") || userId.startsWith("84161") || userId.startsWith("84162") || userId.startsWith("84163") || userId.startsWith("84164")
                || userId.startsWith("84165") || userId.startsWith("84166") || userId.startsWith("84167") || userId.startsWith("84168") || userId.startsWith("84169")
                || userId.startsWith("84120") || userId.startsWith("84121") || userId.startsWith("84122") || userId.startsWith("84123")
                || userId.startsWith("84124") || userId.startsWith("84125") || userId.startsWith("84126")
                || userId.startsWith("84127") || userId.startsWith("84128") || userId.startsWith("84129")) && userId.length() == 12)
                && !((userId.startsWith("+84160") || userId.startsWith("+84161") || userId.startsWith("+84162") || userId.startsWith("+84163") || userId.startsWith("+84164")
                || userId.startsWith("+84165") || userId.startsWith("+84166") || userId.startsWith("+84167") || userId.startsWith("+84168") || userId.startsWith("+84169")
                || userId.startsWith("+84120") || userId.startsWith("+84121") || userId.startsWith("+84122") || userId.startsWith("+84123")
                || userId.startsWith("+84124") || userId.startsWith("+84125") || userId.startsWith("+84126")
                || userId.startsWith("+84127") || userId.startsWith("+84128") || userId.startsWith("+84129"))
                && userId.length() == 13)) {
            check = -3;
            //"Đầu số không hợp lệ";
            return check;
        } else {
            check = 1;
        }
        return check;
    }

    public static String getTimeString() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = "HH:mm:ss";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(cal.getTime());
    }

    public static byte[] hexToByte(String s) {
        int l = s.length() / 2;
        byte data[] = new byte[l];
        int j = 0;
        for (int i = 0; i < l; i++) {
            char c = s.charAt(j++);
            int n, b;
            n = HEXINDEX.indexOf(c);
            b = (n & 0xf) << 4;
            c = s.charAt(j++);
            n = HEXINDEX.indexOf(c);
            b += (n & 0xf);
            data[i] = (byte) b;
        }
        return data;
    }

    public static String stringToHexString(String str) {
        byte[] bytes = null;
        String temp = "";
        try {
            bytes = str.getBytes("US-ASCII");
        } catch (Exception ex) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            temp = temp + Integer.toHexString(bytes[i]);
        }
        return temp;
    }

    public static String stringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuilder strBuffer = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            strBuffer.append(Integer.toHexString((int) chars[i]));
        }
        return strBuffer.toString();
    }

    public static boolean isNumberic(String sNumber) {
        if (sNumber == null || "".equals(sNumber)) {
            return false;
        }
        for (int i = 0; i < sNumber.length(); i++) {
            char ch = sNumber.charAt(i);
            char ch_max = '9';
            char ch_min = '0';
            if (ch < ch_min || ch > ch_max) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkEVN(String userId) {
        String region[] = {
            "18", "19", "20", "210", "211", "22", "23", "240", "241", "25",
            "26", "27", "280", "281", "29", "30", "31", "320", "321", "33",
            "34", "350", "351", "36", "37", "38", "39", "4", "50", "510",
            "511", "52", "53", "54", "55", "56", "57", "58", "59", "60",
            "61", "62", "63", "64", "650", "651", "66", "67", "68", "70",
            "71", "72", "73", "74", "75", "76", "77", "780", "781", "79",
            "8"
        };
        boolean valid = false;
        if (!userId.startsWith("0") || !userId.startsWith("84")) {
            return false;
        }
        String isRegion = "";
        if (userId.startsWith("84")) {
            userId = userId.substring(2);
        } else {
            userId = userId.substring(1);
        }
        int i = 0;
        do {
            if (i >= region.length - 1) {
                break;
            }
            if (userId.startsWith(region[i])) {
                int len = region[i].length();
                String sTmp = userId.substring(len, len + 1);
                if (sTmp.equals("2")) {
                    valid = true;
                } else {
                    valid = false;
                }
                isRegion = region[i];
                break;
            }
            i++;
        } while (true);
        if (valid) {
            if ((userId.startsWith("4") || userId.startsWith("8")) && userId.length() != 8) {
                return false;
            }
            if (!userId.startsWith("4") && !userId.startsWith("8") && userId.length() != 6 + isRegion.length()) {
                return false;
            }
        }
        return valid;
    }

    public static List splitLongMsg(String arg) {
        String result[] = new String[3];
        List v = new ArrayList();
        int segment = 0;
        if (arg.length() <= 160) {
            result[0] = arg;
            v.add(result[0]);
            return v;
        }
        segment = 160;
        StringTokenizer tk = new StringTokenizer(arg, " ");
        String temp = "";
        int j = 0;
        do {
            if (!tk.hasMoreElements()) {
                break;
            }
            String token = (String) tk.nextElement();
            if (temp.equals("")) {
                temp += token;
            } else {
                temp += " " + token;
            }
            if (temp.length() > segment) {
                temp = token;
                j++;
            } else {
                result[j] = temp;
            }
        } while (j != 3);
        for (int i = 0; i < result.length; i++) {
            if (result[i] != null) {
                v.add(result[i]);
            }
        }
        return v;
    }
}
