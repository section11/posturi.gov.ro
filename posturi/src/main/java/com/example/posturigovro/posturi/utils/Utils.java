package com.example.posturigovro.posturi.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by section11 on 04/08/14.
 */
public class Utils {

    String actualizare = "[actualizare]";
    String actualizareStr = "NOTĂ: Anunțul este în curs de actualizare, urmând a fi completat odată cu obținerea informațiilor lipsă din partea instituțiilor implicate în desfășurarea acestui concurs.";
    String biblio = "[biblio]";
    String biblioStr = "Bibliografia necesară în vederea susţinerii concursului poate fi parcursă accesând <a href='$link'>această legătură</a>";
    String cal = "[cal]";
    String calStr = "Concursul se va organiza conform calendarului următor:";
    String calanfp = "[calanfp]";
    String calanfpStr = "Concursul se va organiza la sediul Agenţiei Naţionale a Funcţionarilor Publici conform calendarului următor:";
    String cs = "[cs]";
    String csStr = "Condiţiile specifice necesare în vederea participării la concurs şi a ocupării funcţiei publice sunt:";
    String detalii = "[detalii]";
    String detaliiStr = "Detalii privind condiţiile specifice şi bibliografia de concurs sunt disponibile accesând <a style='text-decoration:none' target='_blank' href=\"$link\">pagina oficială</a>.";

    public Utils(){

    }

    public String replaceContent(String content) {
        StringBuffer stringBuffer = new StringBuffer(content);
        if (content.contains(actualizare)) {
            stringBuffer = replaceBuffer(actualizare, actualizareStr, stringBuffer);
        }

        if(content.contains(biblio)){
            stringBuffer = replaceBuffer(biblio, biblioStr, stringBuffer);
        }

        if(content.contains(cal)){
            stringBuffer = replaceBuffer(cal, calStr, stringBuffer);
        }



        if(content.contains(calanfp)){
            stringBuffer = replaceBuffer(calanfp, calanfpStr, stringBuffer);
        }



        if(content.contains(cs)){
            stringBuffer = replaceBuffer(cs, csStr, stringBuffer);
        }

        if(content.contains(detalii)){
            stringBuffer = replaceBuffer(detalii, detaliiStr, stringBuffer);
        }

        return stringBuffer.toString();
    }

    public StringBuffer replaceBuffer(String what, String with, StringBuffer sb){
        int index = sb.indexOf(what);
        sb.replace(index, index + what.length(), with);
        return sb;
    }

    public String getMiniContinut(String arg) {
        Pattern pattern = Pattern.compile("([\\S]+\\s*){1,25}");
        Matcher matcher = pattern.matcher(arg);
        matcher.find();
        return matcher.group();
    }

    /** Using Calendar - THE CORRECT WAY**/
    public static long daysBetween(Calendar startDate, Calendar endDate) {
        //assert: startDate must be before endDate
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public ArrayList<String> getJudeteInStringArray(ArrayList<Judet> judete){
        ArrayList<String> judeteString = new ArrayList<String>();
        for(Judet judet: judete){
            judeteString.add(judet.getJudet());
        }
        return  judeteString;
    }

    public double getLatitude(String judet, ArrayList<Judet> judete){
        for(Judet jd : judete){
            if(jd.equals(judet)){
                return jd.getLatitude();
            }
        }
        return 0;
    }

    public double getLongitude(String judet, ArrayList<Judet> judete){
        for(Judet jd : judete){
            if(jd.equals(judet)){
                return jd.getLongitude();
            }
        }
        return 0;
    }
}
