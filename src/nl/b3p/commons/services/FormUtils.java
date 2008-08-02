/*
 * B3P Commons Core is a library with commonly used classes for webapps.
 * Included are clieop3, oai, security, struts, taglibs and other
 * general helper classes and extensions.
 *
 * Copyright 2000 - 2008 B3Partners BV
 * 
 * This file is part of B3P Commons Core.
 * 
 * B3P Commons Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Commons Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Commons Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.commons.services;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * @author <a href="chrisvanlith@b3partners.nl">Chris van Lith</a>
 * @version $Revision: 1.7 $ $Date: 2004/08/09 18:27:20 $
 */
public final class FormUtils {

    public static int BooleanToInt(boolean thisbool) {
        return thisbool == false ? 0 : 1;
    }

    public static boolean IntToBoolean(int thisint) {
        return thisint == 0 ? false : true;
    }

    public static Integer BooleanToInteger(boolean thisbool) {
        return thisbool == false ? (new Integer(0)) : (new Integer(1));
    }

    public static boolean IntegerToBoolean(Integer thisint) {
        if (thisint == null) {
            return false;
        }
        return thisint.intValue() == 0 ? false : true;
    }

    public static Date StringToDate(String thisdate, Locale locale) {
        if (thisdate == null || thisdate.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        sdf.applyPattern("dd-MM-yyyy");
        java.util.Date soughtDate = null;
        try {
            soughtDate = sdf.parse(thisdate);
        } catch (Exception pe2) {
            return null;
        }
        return soughtDate;
    }

    public static Date FormStringToDate(String thisdate, Locale locale) {
        if (thisdate == null || thisdate.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        sdf.applyPattern("yyyy-MM-dd");
        java.util.Date soughtDate = null;
        try {
            soughtDate = sdf.parse(thisdate);
        } catch (Exception pe2) {
            return null;
        }
        return soughtDate;
    }

    public static String reformatDateString(String thisdate) {
        if (thisdate == null || thisdate.trim().length() != 8) {
            return null;
        }
        String theYear = thisdate.trim().substring(0, 4);
        String theMonth = thisdate.trim().substring(4, 6);
        String theDay = thisdate.trim().substring(6, 8);
        String newdate = theDay + "-" + theMonth + "-" + theYear;
        return newdate;
    }

    public static String reformatDateBigDecimal(BigDecimal thisdate) {
        if (thisdate == null) {
            return null;
        }
        return reformatDateString(Integer.toString(thisdate.intValue()));
    }

    public static Date reformatBigDecimalToDate(BigDecimal thisdate) {
        if (thisdate == null) {
            return null;
        }
        return FormStringToDate(reformatDateString(Integer.toString(thisdate.intValue())), null);
    }

    public static Date SortStringToDate(String thisdate, Locale locale) {
        return StringToDate(reformatDateString(thisdate), locale);
    }

    public static Date StringToTimestamp(String ts) {
        if (ts == null || ts.trim().length() == 0) {
            return null;
        }
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        java.util.Date soughtDate = null;
        try {
            soughtDate = sdf.parse(ts);
        } catch (Exception pe2) {
            return null;
        }
        return soughtDate;
    }

    public static String DateToString(Date thisdate, Locale locale) {
        if (thisdate != null) {
            SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            sdf.applyPattern("dd-MM-yyyy");
            return sdf.format(thisdate);
        } else {
            return "";
        }
    }

    public static String DateToFormString(Date thisdate, Locale locale) {
        if (thisdate != null) {
            SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            sdf.applyPattern("yyyy-MM-dd");
            return sdf.format(thisdate);
        } else {
            return "";
        }
    }

    public static String TimestampToString(Date thisdate) {
        if (thisdate != null) {
            SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            sdf.applyPattern("yyyy-MM-dd HH:mm:sss");
            return sdf.format(thisdate);
        } else {
            return "";
        }
    }

    public static String DateToSortString(Date thisdate, Locale locale) {
        if (thisdate != null) {
            SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            sdf.applyPattern("yyyyMMdd");
            return sdf.format(thisdate);
        } else {
            return "";
        }
    }

    public static int StringToInt(String thisstring) {
        try {
            return Integer.parseInt(thisstring);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String IntToString(int thisint) {
        return Integer.toString(thisint);
    }

    public static Integer StringToInteger(String thisstring) {
        try {
            return new Integer(thisstring);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long StringToLong(String thisstring) {
        try {
            return new Long(thisstring);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Short StringToShort(String thisstring) {
        try {
            return new Short(thisstring);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String IntegerToString(Integer thisint) {
        if (thisint == null) {
            return "";
        } else {
            return thisint.toString();
        }
    }

    public static String LongToString(Long thislong) {
        if (thislong == null) {
            return "";
        } else {
            return thislong.toString();
        }
    }

    public static String ShortToString(Short thisshort) {
        if (thisshort == null) {
            return "";
        } else {
            return thisshort.toString();
        }
    }

    public static double StringToDbl(String thisstring) {
        if (thisstring == null) {
            return 0.0;
        }
        try {
            return new Double(thisstring).doubleValue();
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static String DblToString(double thisdouble) {
        return (new Double(thisdouble)).toString();
    }

    public static Double StringToDouble(String thisstring) {
        if (thisstring == null) {
            return new Double("0");
        }
        try {
            return new Double(thisstring);
        } catch (NumberFormatException e) {
            return new Double("0");
        }
    }

    public static String DoubleToString(Double thisdouble) {
        if (thisdouble == null) {
            return "";
        } else {
            return thisdouble.toString();
        }
    }

    public static String getString(String thisstring) {
        if (thisstring == null) {
            return "";
        } else {
            return thisstring;
        }
    }

    /**
     * Formatteert d volgens format. Geeft lege String indien d null is.
     */
    public static String formatDouble(Double d, NumberFormat format) {
        if (d == null) {
            return "";
        } else {
            return format.format(d.doubleValue());
        }
    }

    /**
     * Formatteert d volgens format
     */
    public static String formatDouble(double d, NumberFormat format) {
        return format.format(d);
    }

    /**
     * Parst s volgens format. Returnt null indien s null is of een lege string.
     * Throws wel een exception indien s geen double is.
     */
    public static Double parseDoubleAllowNull(String s, NumberFormat format) throws ParseException {
        if (s == null || s.trim().length() == 0) {
            return null;
        } else {
            return new Double(format.parse(s).doubleValue());
        }
    }

    /**
     * Parst s volgens format.
     */
    public static double parseDouble(String s, NumberFormat format) throws ParseException {
        return format.parse(s).doubleValue();
    }

    /**
     * Formatteert bd volgens format. Geeft lege String indien bd null is.
     */
    public static String formatBigDecimal(BigDecimal bd, NumberFormat format) {
        if (bd == null) {
            return "";
        } else {
            return format.format(bd.doubleValue());
        }
    }

    /**
     * Parst s volgens format. Returnt null indien s null is of een lege string.
     * Throws wel een exception indien s geen getal is.
     */
    public static BigDecimal parseBigDecimal(String s, NumberFormat format) throws ParseException {
        if (s == null || s.trim().length() == 0) {
            return null;
        } else {

            return new BigDecimal(format.parse(s).doubleValue());
        }
    }

    public static String nullIfEmpty(String s) {
        if (s != null && s.trim().length() == 0) {
            return null;
        } else {
            return s;
        }
    }

    /**
     * Berekent int waarde van BigDecimal
     * @param val om te zetten BigDecimal, mag null zijn.
     * @return geeft int waarde van val of 0 indien null
     */
    public static int intValue(BigDecimal val) {
        int tempInt = 0;
        if (val != null) {
            tempInt = val.intValue();
        }
        return tempInt;
    }

    /**
     * Berekent float waarde van BigDecimal
     * @param val om te zetten BigDecimal, mag null zijn.
     * @return geeft float waarde van val of 0.0f indien null
     */
    public static float floatValue(BigDecimal val) {
        float tempFloat = 0.0f;
        if (val != null) {
            tempFloat = val.floatValue();
        }
        return tempFloat;
    }

    /**
     * Berekent String waarde van BigDecimal.
     * @param val om te zetten BigDecimal, mag null zijn.
     * @return geeft String waarde van val of "" indien null
     */
    public static String stringValue(BigDecimal val) {
        String tempString = "";
        if (val != null) {
            tempString = val.toString();
        }
        return tempString;
    }

    /**
     * Bepaalt of 2 BigDecimals gelijk zijn. Indien beide
     * null zijn, zijn ze gelijk. Indien slechts een van
     * beide null is, dan zijn ze ongelijk.
     * @param val1 te vergelijken BigDecimal, mag null zijn.
     * @param val2 te vergelijken BigDecimal, mag null zijn.
     * @return 0, indien gelijk, >0, indien val1>val2 en <0 bij omgekeerde
     */
    public static boolean equalBD(BigDecimal val1, BigDecimal val2) {
        if (val1 == null && val2 == null) {
            return true;
        }
        if ((val1 == null && val2 != null) || (val2 == null && val1 != null)) {
            return false;
        }
        return (val1.compareTo(val2) == 0);
    }

    /**
     * Bepaalt BigDecimal op basis van string. Indien ff null
     * is, dan is resultaat null. Indien ff niet omgezet kan
     * worden in een BigDecimal dan is het resultaat 0.
     * @param ff een string die een BigDecimal initialiseert.
     * @return een BigDecimal of null
     */
    public static BigDecimal bdValue(String ff) {
        if (ff == null) {
            return null;
        }
        BigDecimal retval = null;
        try {
            retval = new BigDecimal(ff);
        } catch (NumberFormatException nfe) {
            retval = new BigDecimal("0");
        }
        return retval;
    }

    /**
     * Bepaalt BigDecimal op basis van string. Indien ff null
     * is, dan is resultaat null. Indien ff niet omgezet kan
     * worden in een BigDecimal dan is het resultaat ook null.
     * @param ff een string die een BigDecimal initialiseert.
     * @return een BigDecimal of null
     */
    public static BigDecimal bdValueNull(String ff) {
        if (ff == null) {
            return null;
        }
        BigDecimal retval = null;
        try {
            retval = new BigDecimal(ff);
        } catch (NumberFormatException nfe) {
        }
        return retval;
    }

    /**
     * Geeft altijd een BigDecimal ook indien null (=0).
     * @param bD een BigDecimal of null
     * @return geeft bD of 0, retourneert dus nooit null.
     */
    public static BigDecimal bdSure(BigDecimal bD) {
        if (bD == null) {
            return new BigDecimal("0");
        }
        return bD;
    }

    /**
     * Vermenigvuldigt een BigDecimal met een factor. De factor
     * wordt omgezet in een BigDecimal met een scale van 5.
     * @param val BigDecimal
     * @param factor te vermenigvuldigen factor
     * @return retourneert nooit null
     */
    public static BigDecimal multiplyFactor(BigDecimal val, float factor) {
        int scale = 5;
        return multiplyFactor(val, factor, scale);
    }

    public static BigDecimal multiplyFactor(BigDecimal val, float factor, int scale) {
        if (val == null) {
            return new BigDecimal("0");
        }
        BigDecimal bdFactor = new BigDecimal(factor);
        BigDecimal result = val.multiply(bdFactor);
        return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * gemaksfunctie die test of een string niet null of leeg is.
     * @param astr te testen string
     * @return true indien leeg of null
     */
    public static boolean nullOrEmpty(String astr) {
        return (astr == null || astr.length() == 0);
    }

    public static String[] getStringArray(DynaValidatorForm dynaform, String param) {
        return (String[]) dynaform.get(param);
    }

    public static int getInteger(DynaValidatorForm dynaform, String param) {
        Integer i = (Integer) dynaform.get(param);
        return i != null ? i.intValue() : -1;
    }

    public static boolean getBoolean(DynaValidatorForm dynaform, String param) {
        Boolean bo = (Boolean) dynaform.get(param);
        return bo != null && bo.booleanValue() ? true : false;
    }
}
