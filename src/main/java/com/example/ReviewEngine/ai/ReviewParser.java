package com.example.ReviewEngine.ai;

import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import org.springframework.stereotype.Component;


@Component
public class ReviewParser {

    public Review parse(String response, Product product){

        String reviewText = extractValue(response, "REVIEW:");
        String reviewWriter = extractValue(response, "WRITER:");
        String rating = extractValue(response, "RATING:");

        if (reviewText != null) reviewText = reviewText.trim().replaceAll("^\"|\"$", "");
        if (reviewWriter != null) reviewWriter = reviewWriter.trim().replaceAll("^\"|\"$", "");
        if (rating != null) rating = rating.trim();



        int ratingValue = 5;
        try {
            ratingValue = Integer.parseInt(rating);
        } catch (NumberFormatException e) {
            System.out.println("Kunde inte tolka betyget som ett heltal. Använder standardvärde 5.");
        }

        return Review.builder()
                .product(product)
                .reviewerName(reviewWriter)
                .text(reviewText)
                .rating(ratingValue)
                .build();
        }

    private static String extractValue(String text, String prefix){
        int startIndex = text.indexOf(prefix);
        if(startIndex < 0 ) return null;

        startIndex += prefix.length();
        int endIndex = text.indexOf("\n", startIndex);

        return (endIndex < 0  ? text.substring(startIndex) : text.substring(startIndex, endIndex)).trim();
    }
}
