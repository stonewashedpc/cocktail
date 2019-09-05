package com.stonewashedpc.cocktailmaker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import com.stonewashedpc.cocktailmaker.ingredients.Ingredient;
import com.stonewashedpc.cocktailmaker.ingredients.IngredientBoundary;
import com.stonewashedpc.cocktailmaker.pumps.Pump;
import com.stonewashedpc.cocktailmaker.pumps.PumpBoundary;
import com.stonewashedpc.cocktailmaker.recipies.Recipe;
import com.stonewashedpc.cocktailmaker.recipies.RecipeBoundary;

@Named
@SessionScoped
public class Main implements Serializable {

	private static final Logger LOG = Logger.getLogger(Main.class.getSimpleName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Recipe> recipes;

	private List<Ingredient> ingredients;

	private List<Pump> pumps;

	private int recipeId = 1;

	private String recipeName = "";

	@Inject
	private RecipeBoundary recipeBoundary;
	@Inject
	private IngredientBoundary ingredientBoundary;
	@Inject
	private PumpBoundary pumpBoundary;

	@PostConstruct
	private void init() {
		reloadDb();
	}

	private void reloadDb() {
		recipes = recipeBoundary.findPossibleRecipes();
		pumps = pumpBoundary.findPumps();
		allIngredients = ingredientBoundary.findIngredients();
	}

	public List<Pump> getPumps() {
		return pumps;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public int getRecipeId() {
		return recipeId;
	}

	public String onRecipeClick() {
		// Get parameter (recipe-)id (f:param)
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		recipeId = Integer.valueOf(params.get("id"));
		// Find all ingredients for this recipe(-id)
		ingredients = ingredientBoundary.findIngredientsByRecipe(recipeId);
		// Get Name of this recipe(-id)
		Optional<Recipe> recipe = recipes.stream().filter(r -> r.getId() == recipeId).findFirst();
		recipeName = recipe.get().getName();
		// Find Recipe Amounts
		// amounts = ingredientBoundary.findIngredientAmounts(recipe_id);
		// Redirect to view.xhtml
		return "view.xhtml";
	}
	
	// *******************************************
	// ** This is part of the view.xhtml
	// *******************************************
	
	private float drinkSize;
	
	public float getDrinkSize() {
		return drinkSize;
	}

	public void setDrinkSize(float amount) {
		this.drinkSize = amount;
	}
	
	private float ingredientAmountDenominator;
	
	public void onMixClick() {

		LOG.info("starting onMixClick");

		final Map<Integer, Pin> gpio_map = new HashMap<Integer, Pin>();
		
		gpio_map.put(1, RaspiPin.GPIO_01);
		gpio_map.put(2, RaspiPin.GPIO_02);
		gpio_map.put(3, RaspiPin.GPIO_03);
		gpio_map.put(4, RaspiPin.GPIO_04);
		gpio_map.put(5, RaspiPin.GPIO_05);
		gpio_map.put(6, RaspiPin.GPIO_06);
		gpio_map.put(7, RaspiPin.GPIO_07);
		gpio_map.put(8, RaspiPin.GPIO_08);
		gpio_map.put(9, RaspiPin.GPIO_09);
		gpio_map.put(10, RaspiPin.GPIO_10);
		gpio_map.put(11, RaspiPin.GPIO_11);
		gpio_map.put(12, RaspiPin.GPIO_12);
		gpio_map.put(13, RaspiPin.GPIO_13);
		gpio_map.put(14, RaspiPin.GPIO_14);
		gpio_map.put(15, RaspiPin.GPIO_15);
		gpio_map.put(16, RaspiPin.GPIO_16);

		final GpioController gpio = GpioFactory.getInstance();
		
		ingredientAmountDenominator = 0;
		ingredients.forEach(ingredient -> {
			ingredientAmountDenominator += ingredient.getAmount();
		});

		ingredients.forEach(ingredient -> {

			LOG.log(Level.INFO,
					ingredient.getName() + " Id: " + ingredient.getId() + " Amount: " + ingredient.getAmount());

			int id = ingredient.getId();
			float ingredientAmountNominator = ingredient.getAmount();

			Optional<Pump> optional_pump = pumps.stream().filter(p -> p.getIngredientId() == id).findFirst();

			Pump pump = optional_pump.get();

			float pump_speed = pump.getPumpSpeed();

			int pump_gpio = pump.getGPIO();
			
			//Calculate ingredient's share of recipe
			//Multiply by overall drink size to get ingredient amount in ml
			//Get time in milliseconds by multiplying by 60000 and dividing by pump's pumping speed
			float time = ((ingredientAmountNominator/ingredientAmountDenominator) * drinkSize) * 60000 / pump_speed;

			LOG.log(Level.INFO, "filling {0} using pump {1} for {2}ms",
					new Object[] { ingredient.getName(), pump.getIngredientId(), time });

			final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(gpio_map.get(pump_gpio), "Pump",
					PinState.HIGH);

			pin.setShutdownOptions(true, PinState.HIGH);

			try {
				pin.low();
				LOG.log(Level.INFO, "Start");

				Thread.sleep((long) (time));

				pin.high();
				LOG.log(Level.INFO, "Stop");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gpio.unprovisionPin(pin);
		});

		gpio.shutdown();
	}
	
	// *******************************************
	// ** This is part of the configuration.xhtml
	// *******************************************
	private int flowrate;
	private int gpio;
	private int ingredient;
	
	
	private List<Ingredient> allIngredients;
	private int[] allGpios = {1,2,3,4,5,6,7,8};
	
	
	public void onDeletePumpClick() {

		// Get parameter gpio (f:param)
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
		int gpio = Integer.valueOf(params.get("gpio"));
		
		//Delete Pump
		pumpBoundary.deletePump(gpio);
		
		//Reload possible recipes and pumps from database
		reloadDb();
	}
	
	public void onAddPumpClick() {
		
		pumpBoundary.addPump(gpio, ingredient, flowrate);
		//Reload possible recipes and pumps from database
		reloadDb();
	}
	
	public void pumpPulse(int time) {

		LOG.info("starting pumpPulse");

		final Map<Integer, Pin> gpio_map = new HashMap<Integer, Pin>();
		
		gpio_map.put(1, RaspiPin.GPIO_01);
		gpio_map.put(2, RaspiPin.GPIO_02);
		gpio_map.put(3, RaspiPin.GPIO_03);
		gpio_map.put(4, RaspiPin.GPIO_04);
		gpio_map.put(5, RaspiPin.GPIO_05);
		gpio_map.put(6, RaspiPin.GPIO_06);
		gpio_map.put(7, RaspiPin.GPIO_07);
		gpio_map.put(8, RaspiPin.GPIO_08);
		gpio_map.put(9, RaspiPin.GPIO_09);
		gpio_map.put(10, RaspiPin.GPIO_10);
		gpio_map.put(11, RaspiPin.GPIO_11);
		gpio_map.put(12, RaspiPin.GPIO_12);
		gpio_map.put(13, RaspiPin.GPIO_13);
		gpio_map.put(14, RaspiPin.GPIO_14);
		gpio_map.put(15, RaspiPin.GPIO_15);
		gpio_map.put(16, RaspiPin.GPIO_16);

		final GpioController gpio = GpioFactory.getInstance();

		pumps.forEach(pump -> {

			int pump_gpio = pump.getGPIO();

			LOG.log(Level.INFO, "Pump {0} on for {1}ms",
					new Object[] { pump.getGPIO(), time*1000 });

			final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(gpio_map.get(pump_gpio), "Pump",
					PinState.HIGH);

			pin.setShutdownOptions(true, PinState.HIGH);

			try {
				pin.low();
				LOG.log(Level.INFO, "Start");

				Thread.sleep((long) (time*1000));

				pin.high();
				LOG.log(Level.INFO, "Stop");

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gpio.unprovisionPin(pin);
		});

		gpio.shutdown();
	}

	public int getFlowrate() {
		return flowrate;
	}

	public void setFlowrate(int flowrate) {
		this.flowrate = flowrate;
	}

	public int getGpio() {
		return gpio;
	}

	public void setGpio(int gpio) {
		this.gpio = gpio;
	}

	public int getIngredient() {
		return ingredient;
	}

	public void setIngredient(int ingredient) {
		this.ingredient = ingredient;
	}

	public int[] getAllGpios() {
		return allGpios;
	}

	public void setAllGpios(int[] allGpios) {
		this.allGpios = allGpios;
	}

	public List<Ingredient> getAllIngredients() {
		return allIngredients;
	}

	public void setAllIngredients(List<Ingredient> allIngredients) {
		this.allIngredients = allIngredients;
	}
}
