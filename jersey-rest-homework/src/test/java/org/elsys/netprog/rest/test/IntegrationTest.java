package org.elsys.netprog.rest.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import static io.restassured.RestAssured.*;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;

public class IntegrationTest {

	private final String serverBaseAddress = "jersey-rest-homework/game";
	
	@Test
	public void playGame() {
		RestAssured.port = 8080;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
		RestAssured.baseURI = "http://localhost";
		
		System.out.println("Starting new game");
		Response post = given().basePath(serverBaseAddress).post("startGame");
		post.then().statusCode(201);
		String gameId = post.getBody().asString();
		int counter = 0;
		int realNumber = -1;
		for (int i = 1000; i< 10000; ++i) {
			if (i % 100 == 0) {
				assertGame(gameId, false, counter, "****");
			}
			
			Response response = given().basePath(serverBaseAddress).put("guess/" + gameId + "/" + i);
			if (response.getStatusCode() == 200) {
				counter++;
				String returnedGameId = response.body().jsonPath().getString("gameId");
				boolean isSuccess = response.body().jsonPath().getBoolean("success");
				int cowsNumber = response.body().jsonPath().getInt("cowsNumber");
				int bullsNumber = response.body().jsonPath().getInt("bullsNumber");
				int turnsCount = response.body().jsonPath().getInt("turnsCount");
				
				Assert.assertEquals(returnedGameId, gameId);
				Assert.assertEquals(turnsCount, counter);
				
				System.out.println("Call for " + i + " returned " + bullsNumber + " bulls and " + cowsNumber + " cows");
				if (isSuccess) {
					System.out.println("Game end");
					realNumber = i;
					break;
				}
			} else {
				//Assert.assertEquals(response.getStatusCode(), 400);
			}
		}
		
		assertGame(gameId, true, counter, String.valueOf(realNumber));
	}

	@Test
	public void testWrongInput() {
		RestAssured.port = 8080;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
		RestAssured.baseURI = "http://localhost";
		
		System.out.println("Starting new game");
		Response post = given().basePath(serverBaseAddress).post("startGame");
		post.then().statusCode(201);
		String gameId = post.getBody().asString();
		
		given().basePath(serverBaseAddress).put("guess/" + gameId  +"1/1234").then().statusCode(404);

		given().basePath(serverBaseAddress).put("guess/" + gameId  +"/1124").then().statusCode(400);
		given().basePath(serverBaseAddress).put("guess/" + gameId  +"/0123").then().statusCode(400);
		given().basePath(serverBaseAddress).put("guess/" + gameId  +"/1000").then().statusCode(400);
		given().basePath(serverBaseAddress).put("guess/" + gameId  +"/9877").then().statusCode(400);
		given().basePath(serverBaseAddress).put("guess/" + gameId  +"/abcd").then().statusCode(400);
		
		assertGame(gameId, false, 0, "****");
	}
	
	@Test
	public void playTwoGames() {
		RestAssured.port = 8080;
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.defaultParser = Parser.JSON;
		RestAssured.baseURI = "http://localhost";
		
		System.out.println("Starting new game");
		String gameId1 = given().basePath(serverBaseAddress).post("startGame").getBody().asString();
		String gameId2 = given().basePath(serverBaseAddress).post("startGame").getBody().asString();
		
		given().basePath(serverBaseAddress).put("guess/" + gameId1  +"/9845").then().statusCode(200);
		assertGame(gameId1, false, 1, "****");
		given().basePath(serverBaseAddress).put("guess/" + gameId2  +"/1236").then().statusCode(200);
		assertGame(gameId2, false, 1, "****");
		given().basePath(serverBaseAddress).put("guess/" + gameId1  +"/9845").then().statusCode(200);
		assertGame(gameId1, false, 2, "****");
		assertGame(gameId2, false, 1, "****");
		
	}
	
	private void assertGame(String gameId, boolean success, int turnsCount, String secret) {
		System.out.println("Check last game with secret " + secret);
		Response gamesResponse = given().basePath(serverBaseAddress).get("games");
		
		List<Object> list = gamesResponse.body().jsonPath().getList(".");
		for (Object mapObject : list)
		{
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) mapObject;
			
			String currentGameId = (String) map.get("gameId");
			
			if (currentGameId.equals(gameId)) {
				boolean lastGameSuccess = (Boolean) map.get("success");
				int lastTurnsCount = (Integer) map.get("turnsCount");
				String lastSecret = (String) map.get("secret");
				
				Assert.assertEquals(success, lastGameSuccess);
				Assert.assertEquals(turnsCount, lastTurnsCount);
				Assert.assertEquals(secret, lastSecret); 
			}
		}
	}
}
