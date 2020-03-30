package com.slotvinskiy;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Task2 {

    public static final LocalDate CURRENT_DATE = LocalDate.now();
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");
    public static final LocalDate PB_ESTABLISH_DATE = LocalDate.parse("19.03.1992", FORMATTER);
    public static final String URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";

    public static void main(String[] args) {

        String date = "";
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter date in format day.month.year: ");
            do {
                date = scanner.nextLine();
            } while (!isCorrect(date));
        } catch (Exception e) {
            showErrorMessage();
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL + date)
                .method("GET", null)
                .addHeader("Content-Type", "")
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            Gson gson = new Gson();
            ClassForParseResponseJson responseJson = gson.fromJson(body.string(), ClassForParseResponseJson.class);
            System.out.println(responseJson.get("USD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isCorrect(String date) {
        if (date == null || date.equals("") || date.length() < 8) {
            showErrorMessage();
            return false;
        }
        LocalDate inputDate = null;
        try {
            inputDate = LocalDate.parse(date, FORMATTER);
        } catch (Exception e) {
            showErrorMessage();
            return false;
        }
        if (inputDate.isAfter(ChronoLocalDate.from(PB_ESTABLISH_DATE)) &&
                inputDate.isBefore(ChronoLocalDate.from(CURRENT_DATE))) {
            return true;
        } else {
            showErrorMessage();
            return false;
        }
    }

    private static void showErrorMessage() {
        System.out.println("Wrong data format! Try to enter date one more time. Format day.month.year: ");
    }
}


