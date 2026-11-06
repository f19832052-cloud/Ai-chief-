package com.example.data.repository

import com.example.data.model.CookingStep
import com.example.data.model.IngredientItem
import com.example.data.model.Recipe

object AiChefPresetData {

    val sampleRecipes: List<Recipe> = listOf(
        Recipe(
            id = "preset_fried_rice",
            dishName = "Student Chicken & Egg Fried Rice",
            summary = "The ultimate bachelor staple: fragrant day-old rice flash-fried with juicy scrambled eggs, chicken, soy sauce, garlic, and fresh green onions.",
            confidenceScore = 96,
            confidenceExplanation = "Identified distinct wok-tossed yellow rice grains, scrambled egg curds, chopped scallions, and soy caramelization.",
            isFood = true,
            imageUrl = null,
            prepTimeMinutes = 8,
            cookTimeMinutes = 10,
            difficulty = "Beginner",
            defaultServings = 1,
            baseCostPerServingUSD = 1.25,
            visibleObservations = listOf(
                "Fluffy golden rice grains with toasted edges",
                "Scrambled yellow and white egg curds",
                "Finely sliced green scallions/spring onions",
                "Seared protein morsels with savory sheen"
            ),
            inferredIngredients = listOf(
                "Light soy sauce (gives signature umami caramelization)",
                "Toasted sesame oil (finishing aroma)",
                "Minced garlic and neutral cooking oil",
                "A dash of white pepper or black pepper"
            ),
            ingredients = listOf(
                IngredientItem("Cooked cold rice (day-old preferred)", 200.0, "g", "Pantry", true, "Freshly cooked rice spread flat on a plate to cool for 15 mins"),
                IngredientItem("Large Eggs", 2.0, "pcs", "Protein", true, "1 egg + 50g canned chickpeas for tighter budget"),
                IngredientItem("Boneless chicken breast or thigh", 80.0, "g", "Protein", false, "Substitute with sausage, hot dog, or omit for vegetarian"),
                IngredientItem("Garlic cloves, minced", 2.0, "pcs", "Produce", true, "0.5 tsp garlic powder"),
                IngredientItem("Soy sauce (light/regular)", 1.5, "tbsp", "Pantry", true, "Pinch of salt + bouillon cube"),
                IngredientItem("Cooking oil", 1.0, "tbsp", "Pantry", true, null),
                IngredientItem("Spring onions / scallions", 2.0, "pcs", "Produce", false, "Chopped regular yellow onion"),
                IngredientItem("Black or white pepper", 0.5, "tsp", "Spices", false, null),
                IngredientItem("Sesame oil (optional)", 0.5, "tsp", "Pantry", false, null)
            ),
            instructions = listOf(
                CookingStep(
                    stepNumber = 1,
                    title = "Prep Rice & Protein",
                    instruction = "Break up any clumps in the cold cooked rice using your fingers or a spoon. Slice the chicken into bite-sized 1cm cubes. Beat eggs in a small mug with a pinch of salt.",
                    timerSeconds = 120,
                    proTip = "Dry cold rice is key: fresh hot rice gets mushy when fried."
                ),
                CookingStep(
                    stepNumber = 2,
                    title = "Scramble the Eggs",
                    instruction = "Heat 1 tsp oil in a wide frying pan over high heat. Pour in beaten eggs, scramble softly for 45-60 seconds until 80% set, then immediately remove onto a plate.",
                    timerSeconds = 60,
                    proTip = "Don't overcook eggs now; they will rejoin the pan at the very end."
                ),
                CookingStep(
                    stepNumber = 3,
                    title = "Sear Chicken & Garlic",
                    instruction = "Add remaining oil to the smoking hot pan. Add chicken pieces and minced garlic. Sear undisturbed for 2 minutes, then flip and toss until golden brown and cooked through.",
                    timerSeconds = 180,
                    proTip = "Ensure chicken reaches 74°C (165°F) safe internal temperature."
                ),
                CookingStep(
                    stepNumber = 4,
                    title = "Wok-Toss the Rice",
                    instruction = "Dump the cold rice into the sizzling pan. Press rice into the pan surface for 30 seconds to get toasty crispy grains, then stir vigorously. Drizzle soy sauce around the rim of the pan so it caramelizes before hitting the rice.",
                    timerSeconds = 180,
                    proTip = "Drizzling sauce on hot pan edges creates restaurant-style wok aroma (wok hei)."
                ),
                CookingStep(
                    stepNumber = 5,
                    title = "Combine & Finish",
                    instruction = "Return scrambled eggs to pan, chopping them with the spatula into the rice. Toss in sliced scallions and pepper. Stir-fry for 1 final minute. Drizzle sesame oil and remove from heat.",
                    timerSeconds = 60,
                    proTip = "Serve hot with a splash of hot chili sauce or cucumber slices."
                )
            ),
            equipment = listOf("Wide frying pan or wok", "Spatula / wooden spoon", "Chef's knife & cutting board"),
            allergyWarnings = listOf("Contains Soy and Eggs. Use coconut aminos for soy allergy."),
            foodSafetyNote = "Poultry safe cooking temp: 74°C (165°F). Never leave cooked rice at room temperature for over 2 hours; Bacillus cereus spores can develop rapidly.",
            studentBudgetTips = listOf(
                "Buy 5kg bulk rice instead of small bags to cut starch costs by 60%",
                "Eggs are the cheapest high-quality protein per gram for students",
                "Keep chopped scallions and garlic cloves in freezer bags for zero food waste"
            ),
            youtubeQuery = "how to cook easy egg fried rice beginner recipe",
            isFavorite = false
        ),
        Recipe(
            id = "preset_dal_tadka",
            dishName = "10-Minute Bengali Dal Tadka & Rice",
            summary = "Comfort in a bowl: creamy yellow masoor lentils gently spiced with turmeric and finished with sizzling garlic, dried red chilies, and mustard seed tadka.",
            confidenceScore = 98,
            confidenceExplanation = "Identified vibrant golden lentil broth, softened halved masoor dal, garlic slivers, and chili tadka.",
            isFood = true,
            imageUrl = null,
            prepTimeMinutes = 5,
            cookTimeMinutes = 15,
            difficulty = "Beginner",
            defaultServings = 1,
            baseCostPerServingUSD = 0.55,
            visibleObservations = listOf(
                "Smooth golden-yellow lentil porridge",
                "Browned sliced garlic cloves on surface",
                "Whole dried red chili pepper float",
                "Flecked mustard / cumin seeds and fresh cilantro"
            ),
            inferredIngredients = listOf(
                "Red lentils (Masoor Dal) - cooks fast with zero soaking",
                "Turmeric powder for vibrant anti-inflammatory color",
                "Mustard oil or ghee for rich tempering aroma",
                "Cumin seeds and pinch of salt"
            ),
            ingredients = listOf(
                IngredientItem("Red Lentils (Masoor dal)", 80.0, "g", "Pantry", true, "Yellow moong dal or green split lentils"),
                IngredientItem("Water", 350.0, "ml", "Pantry", true, null),
                IngredientItem("Turmeric powder", 0.5, "tsp", "Spices", true, null),
                IngredientItem("Salt", 0.75, "tsp", "Spices", true, null),
                IngredientItem("Garlic cloves, thinly sliced", 3.0, "pcs", "Produce", true, null),
                IngredientItem("Whole cumin seeds", 0.5, "tsp", "Spices", false, null),
                IngredientItem("Dried red chili or green chili", 1.0, "pcs", "Produce", false, "Pinch of chili flakes"),
                IngredientItem("Mustard oil or ghee/cooking oil", 1.0, "tbsp", "Pantry", true, null),
                IngredientItem("Fresh coriander / cilantro (optional)", 1.0, "tbsp", "Produce", false, null)
            ),
            instructions = listOf(
                CookingStep(
                    stepNumber = 1,
                    title = "Rinse Lentils",
                    instruction = "Rinse masoor dal in water 2-3 times until the water runs mostly clear. Drain water.",
                    timerSeconds = 60,
                    proTip = "Masoor dal does not require any soaking; it melts into creaminess in 10 minutes."
                ),
                CookingStep(
                    stepNumber = 2,
                    title = "Boil Lentils with Turmeric",
                    instruction = "In a small pot, add rinsed dal, 350ml water, turmeric powder, and salt. Bring to a rapid boil, skim off any white foam from the surface, then lower heat and simmer for 10-12 minutes until soft.",
                    timerSeconds = 600,
                    proTip = "Whisk with a spoon or dal-ghutni for silky consistency."
                ),
                CookingStep(
                    stepNumber = 3,
                    title = "Prepare the Sizzling Tadka",
                    instruction = "In a small pan, heat 1 tbsp mustard oil or ghee until shimmering. Add cumin seeds, sliced garlic, and dried red chili. Fry for 45-60 seconds until garlic is golden brown and fragrant.",
                    timerSeconds = 60,
                    proTip = "Watch closely; garlic goes from golden perfection to burnt very quickly."
                ),
                CookingStep(
                    stepNumber = 4,
                    title = "Tempering & Serve",
                    instruction = "Carefully pour the sizzling hot garlic-chili oil into the simmering dal (it will hiss deliciously!). Stir gently, garnish with chopped coriander, and ladle over hot steamed rice.",
                    timerSeconds = 30,
                    proTip = "Cover pot with lid for 2 minutes after tempering to trap the smoky aromatics."
                )
            ),
            equipment = listOf("Small saucepan / pot", "Small frying pan for tempering (tadka pan)", "Whisk or wooden spoon"),
            allergyWarnings = listOf("Naturally vegan and gluten-free. Free of major common allergens."),
            foodSafetyNote = "Lentils store safely in an airtight container in the fridge for up to 4 days.",
            studentBudgetTips = listOf(
                "Lentils cost under ৳130 / $1 per kilogram and yield 12+ meals",
                "Packed with 18g plant protein per cooked bowl",
                "Pairs with leftover boiled potatoes (Aloo Bhorta) for an iconic student dinner"
            ),
            youtubeQuery = "quick masoor dal recipe for students beginner",
            isFavorite = false
        ),
        Recipe(
            id = "preset_pasta",
            dishName = "Creamy Garlic & Tomato Penne",
            summary = "A silky Italian comfort classic built from cheap pantry heroes: canned crushed tomatoes, sweet garlic, oregano, and pasta finished with a touch of butter or cheese.",
            confidenceScore = 93,
            confidenceExplanation = "Identified tubular penne pasta coated in rich orange-red emulsified tomato-garlic sauce.",
            isFood = true,
            imageUrl = null,
            prepTimeMinutes = 5,
            cookTimeMinutes = 12,
            difficulty = "Beginner",
            defaultServings = 1,
            baseCostPerServingUSD = 1.10,
            visibleObservations = listOf(
                "Al dente penne pasta holding sauce in ridges",
                "Emulsified glossy tomato and garlic sauce",
                "Sprinkled dried oregano and cracked black pepper",
                "Melted grated cheese layer on top"
            ),
            inferredIngredients = listOf(
                "Canned crushed plum tomatoes or tomato passata",
                "Starchy salted pasta water (the emulsification secret!)",
                "Garlic sauteed gently in oil/butter without burning",
                "Pinch of sugar to balance tomato acidity"
            ),
            ingredients = listOf(
                IngredientItem("Penne or Rigatoni pasta", 100.0, "g", "Pantry", true, "Any dry pasta shapes (spaghetti, macaroni, fusilli)"),
                IngredientItem("Canned tomato puree or chopped tomatoes", 150.0, "g", "Pantry", true, "3 ripe fresh tomatoes blended"),
                IngredientItem("Garlic cloves, crushed and minced", 3.0, "pcs", "Produce", true, null),
                IngredientItem("Olive oil or butter", 1.5, "tbsp", "Pantry", true, "Regular cooking oil"),
                IngredientItem("Dried oregano or Italian herbs", 0.5, "tsp", "Spices", true, "Fresh basil leaves"),
                IngredientItem("Salt", 1.0, "tsp", "Spices", true, null),
                IngredientItem("Sugar", 0.25, "tsp", "Pantry", false, null),
                IngredientItem("Parmesan or grated cheese", 2.0, "tbsp", "Dairy", false, "Splash of regular milk for creaminess")
            ),
            instructions = listOf(
                CookingStep(
                    stepNumber = 1,
                    title = "Boil the Pasta",
                    instruction = "Bring a pot of salted water to a rolling boil. Add penne and cook for 9-10 minutes until al dente (tender with a slight bite). CRITICAL: Scoop out 1/2 cup of pasta water before draining!",
                    timerSeconds = 540,
                    proTip = "Never rinse cooked pasta with tap water; you wash away the starch that grips the sauce."
                ),
                CookingStep(
                    stepNumber = 2,
                    title = "Saute Garlic in Butter/Oil",
                    instruction = "While pasta cooks, heat oil/butter in a wide pan over medium-low heat. Add minced garlic and fry for 1 minute until fragrant and lightly golden (do not let it brown dark).",
                    timerSeconds = 60,
                    proTip = "Gentle heat prevents bitter burnt garlic."
                ),
                CookingStep(
                    stepNumber = 3,
                    title = "Simmer the Tomato Sauce",
                    instruction = "Pour in tomato puree, dried oregano, salt, black pepper, and pinch of sugar. Simmer over medium heat for 5-6 minutes until thickened and oil separates slightly.",
                    timerSeconds = 300,
                    proTip = "The pinch of sugar cuts natural tomato acidity instantly."
                ),
                CookingStep(
                    stepNumber = 4,
                    title = "Emulsify with Pasta Water",
                    instruction = "Add drained penne to the sauce pan. Pour in 3-4 tbsp of reserved hot starchy pasta water. Toss and stir vigorously over medium heat for 1 minute until sauce turns glossy and clings to every tube.",
                    timerSeconds = 60,
                    proTip = "Starch + fat + heat creates an Italian restaurant emulsion without expensive heavy cream."
                ),
                CookingStep(
                    stepNumber = 5,
                    title = "Finish with Cheese",
                    instruction = "Turn off heat, stir in grated cheese, taste, and adjust salt. Serve immediately while steaming.",
                    timerSeconds = 30,
                    proTip = "Add a hard-boiled egg or pan-seared sausage for extra bachelor protein."
                )
            ),
            equipment = listOf("Pasta boiling pot", "Skillet or sauté pan", "Colander / strainer", "Measuring cup"),
            allergyWarnings = listOf("Contains Gluten (wheat pasta) and Dairy (butter/cheese). Use gluten-free pasta and olive oil for dietary needs."),
            foodSafetyNote = "Reheat pasta thoroughly to 74°C (165°F) with a splash of water to restore moisture.",
            studentBudgetTips = listOf(
                "Dry pasta stores indefinitely in pantry cupboards",
                "Store-brand canned tomatoes are identical in quality to premium brands",
                "Pasta water is completely free and replaces expensive cream"
            ),
            youtubeQuery = "beginner creamy tomato pasta without heavy cream",
            isFavorite = false
        ),
        Recipe(
            id = "preset_shakshuka",
            dishName = "One-Pan Shakshuka with Jammy Eggs",
            summary = "A vibrant Mediterranean and North African breakfast-for-dinner: soft poached eggs nestled in a spiced stew of sweet bell peppers, crushed tomatoes, cumin, and garlic.",
            confidenceScore = 95,
            confidenceExplanation = "Identified whole poached eggs with runny yolks resting in bubbling chunky tomato and bell pepper sauce.",
            isFood = true,
            imageUrl = null,
            prepTimeMinutes = 7,
            cookTimeMinutes = 14,
            difficulty = "Beginner",
            defaultServings = 1,
            baseCostPerServingUSD = 1.35,
            visibleObservations = listOf(
                "Bright red bubbling tomato stew with diced peppers",
                "Whole poached eggs with tender whites and soft yolks",
                "Crushed cumin and paprika specks",
                "Fresh green parsley / herb garnish"
            ),
            inferredIngredients = listOf(
                "Ground cumin and smoked or sweet paprika",
                "Yellow onion and garlic base sauté",
                "Sweet bell pepper (capsicum) diced small",
                "Olive oil or vegetable oil"
            ),
            ingredients = listOf(
                IngredientItem("Large Eggs", 2.0, "pcs", "Protein", true, null),
                IngredientItem("Canned diced tomatoes or 2 ripe fresh tomatoes", 180.0, "g", "Produce", true, null),
                IngredientItem("Bell pepper (red or green), diced", 0.5, "pcs", "Produce", true, "1 small onion + carrot"),
                IngredientItem("Yellow onion, diced", 0.5, "pcs", "Produce", true, null),
                IngredientItem("Garlic cloves, minced", 2.0, "pcs", "Produce", true, null),
                IngredientItem("Ground cumin", 0.75, "tsp", "Spices", true, null),
                IngredientItem("Paprika or chili powder", 0.5, "tsp", "Spices", true, null),
                IngredientItem("Olive oil or cooking oil", 1.0, "tbsp", "Pantry", true, null),
                IngredientItem("Crusty bread or toasted flatbread/roti", 2.0, "pcs", "Pantry", false, "Steamed rice or pita")
            ),
            instructions = listOf(
                CookingStep(
                    stepNumber = 1,
                    title = "Sauté Vegetables",
                    instruction = "Heat oil in a skillet over medium heat. Add chopped onions and bell peppers. Cook for 4-5 minutes until softened and edges are lightly caramelized. Add garlic, cumin, and paprika; stir for 30 seconds.",
                    timerSeconds = 300,
                    proTip = "Caramelizing onions builds natural sweetness to balance tomatoes."
                ),
                CookingStep(
                    stepNumber = 2,
                    title = "Simmer Sauce",
                    instruction = "Pour in diced tomatoes and 2 tbsp water. Season with salt and pepper. Simmer uncovered for 5 minutes until sauce is thick and chunky.",
                    timerSeconds = 300,
                    proTip = "Sauce must be thick enough to hold wells for the eggs."
                ),
                CookingStep(
                    stepNumber = 3,
                    title = "Make Wells & Crack Eggs",
                    instruction = "Use the back of a large spoon to create 2 small wells/pockets in the bubbling sauce. Gently crack an egg into each well. Season egg tops with a pinch of salt.",
                    timerSeconds = 60,
                    proTip = "Crack eggs into a small ramekin or cup first so shells don't fall into the hot sauce."
                ),
                CookingStep(
                    stepNumber = 4,
                    title = "Cover & Poach",
                    instruction = "Turn heat to low, cover the skillet with a lid or plate, and let cook for 4-5 minutes until egg whites are fully set but yolks are still delightfully runny and jammy.",
                    timerSeconds = 270,
                    proTip = "Check at minute 4; egg yolks cook fast once covered."
                ),
                CookingStep(
                    stepNumber = 5,
                    title = "Serve Straight from Pan",
                    instruction = "Remove lid, sprinkle parsley, and take the skillet directly to your table with toasted bread or roti for scooping. Zero dishes to wash!",
                    timerSeconds = 30,
                    proTip = "Dip warm bread directly into the yolk and sauce."
                )
            ),
            equipment = listOf("Medium frying pan with lid (or plate cover)", "Wooden spoon", "Knife and cutting board"),
            allergyWarnings = listOf("Contains Eggs. Bread contains gluten (substitute corn tortillas or rice for gluten-free)."),
            foodSafetyNote = "Ensure egg whites are completely opaque and set (min 71°C / 160°F) to eliminate salmonella risks.",
            studentBudgetTips = listOf(
                "One-pan meals save cooking gas and minimize bachelor cleaning time",
                "Eggs provide complete amino acids on a strict student budget",
                "Great way to use up half-empty bell peppers and limp celery in the fridge"
            ),
            youtubeQuery = "simple beginner shakshuka one pan recipe",
            isFavorite = false
        ),
        Recipe(
            id = "preset_tofu_stirfry",
            dishName = "Crispy Pan-Seared Tofu & Veggie Stir-Fry",
            summary = "Golden, crunchy tofu cubes coated in a sweet-savory garlic soy glaze tossed with crisp shredded cabbage and carrots. High protein, high fiber, and under $1.40.",
            confidenceScore = 92,
            confidenceExplanation = "Identified crisped golden tofu cubes, wok-tossed cabbage ribbons, carrots, and savory sesame soy glaze.",
            isFood = true,
            imageUrl = null,
            prepTimeMinutes = 10,
            cookTimeMinutes = 10,
            difficulty = "Beginner",
            defaultServings = 1,
            baseCostPerServingUSD = 1.30,
            visibleObservations = listOf(
                "Square golden-brown crusted tofu blocks",
                "Sliced tender-crisp orange carrots and green cabbage",
                "Glazed savory sauce coating vegetables evenly",
                "Toasted white sesame seeds sprinkled"
            ),
            inferredIngredients = listOf(
                "Firm tofu pressed to remove moisture for maximum crispiness",
                "Cornstarch or all-purpose flour for the crunch coating",
                "Soy sauce, brown sugar, and garlic glaze",
                "Neutral high-heat cooking oil"
            ),
            ingredients = listOf(
                IngredientItem("Firm Tofu", 150.0, "g", "Protein", true, "Paneer or 2 boiled eggs cut in half"),
                IngredientItem("Cornstarch or flour", 1.5, "tbsp", "Pantry", true, null),
                IngredientItem("Shredded cabbage", 100.0, "g", "Produce", true, "Any seasonal leafy greens"),
                IngredientItem("Carrot, thinly sliced matchsticks", 0.5, "pcs", "Produce", true, null),
                IngredientItem("Soy sauce", 1.5, "tbsp", "Pantry", true, null),
                IngredientItem("Honey or brown sugar", 1.0, "tsp", "Pantry", true, "Regular white sugar"),
                IngredientItem("Garlic & ginger minced", 1.0, "tsp", "Produce", true, null),
                IngredientItem("Cooking oil", 1.5, "tbsp", "Pantry", true, null)
            ),
            instructions = listOf(
                CookingStep(
                    stepNumber = 1,
                    title = "Press & Cube Tofu",
                    instruction = "Cut firm tofu into 2cm cubes. Press between folded paper towels or a clean kitchen cloth for 5 minutes with a book on top to squeeze out excess moisture.",
                    timerSeconds = 300,
                    proTip = "Dry tofu = super crispy tofu. Wet tofu steams instead of frying."
                ),
                CookingStep(
                    stepNumber = 2,
                    title = "Dust with Cornstarch",
                    instruction = "Toss dried tofu cubes gently in cornstarch with a pinch of salt until lightly and evenly coated.",
                    timerSeconds = 60,
                    proTip = "Cornstarch creates a glass-like crunch layer."
                ),
                CookingStep(
                    stepNumber = 3,
                    title = "Pan-Fry to Golden Crisp",
                    instruction = "Heat 1 tbsp oil in a wide skillet over medium-high heat. Add tofu cubes in a single layer. Fry for 2-3 minutes per side until all sides are golden brown and crunchy. Remove to a plate.",
                    timerSeconds = 360,
                    proTip = "Don't crowd the pan; leave space so steam can escape."
                ),
                CookingStep(
                    stepNumber = 4,
                    title = "Stir-Fry Vegetables",
                    instruction = "In the same hot pan, add remaining oil, minced garlic, ginger, sliced carrots, and cabbage. Stir-fry over high heat for 2 minutes until bright and tender-crisp.",
                    timerSeconds = 120,
                    proTip = "Keep vegetables moving quickly so they retain their crunch."
                ),
                CookingStep(
                    stepNumber = 5,
                    title = "Glaze & Combine",
                    instruction = "Mix soy sauce, sugar, and 1 tbsp water in a small cup. Pour into the pan. Toss in the crispy tofu and coat everything in the bubbling glaze for 30 seconds. Serve immediately over rice.",
                    timerSeconds = 45,
                    proTip = "Eat right away while the tofu retains its crunch!"
                )
            ),
            equipment = listOf("Non-stick skillet or wok", "Paper towels", "Spatula"),
            allergyWarnings = listOf("Contains Soy. Substitute tofu with chickpeas or paneer for soy sensitivity."),
            foodSafetyNote = "Refrigerate unused uncooked tofu in fresh water and change water daily for up to 5 days.",
            studentBudgetTips = listOf(
                "Tofu provides 16g clean protein per portion at a fraction of beef or chicken prices",
                "Cabbage is one of the cheapest, longest-lasting fresh vegetables in any student fridge",
                "Leftover stir-fry makes a stellar cold noodle bowl for lunch"
            ),
            youtubeQuery = "how to make crispy tofu stir fry easy beginner",
            isFavorite = false
        ),
        Recipe(
            id = "preset_smash_burger",
            dishName = "Budget Smash Cheeseburger & Crispy Onions",
            summary = "Skip overpriced restaurant delivery: a crispy, lacy-edged ground beef patty smashed super thin on a screaming hot pan with melted cheese, caramelized onions, and secret burger sauce.",
            confidenceScore = 97,
            confidenceExplanation = "Identified smashed beef patty with signature crispy lacy edges, melted cheese skirt, and griddled burger bun.",
            isFood = true,
            imageUrl = null,
            prepTimeMinutes = 5,
            cookTimeMinutes = 6,
            difficulty = "Beginner",
            defaultServings = 1,
            baseCostPerServingUSD = 1.65,
            visibleObservations = listOf(
                "Toasted burger bun with golden butter gloss",
                "Thin smashed beef patty with irregular crispy dark crust",
                "Melted yellow cheddar / American cheese draping over edges",
                "Thinly shaved griddled onions integrated into patty"
            ),
            inferredIngredients = listOf(
                "Ground beef (80/20 lean to fat ratio gives ultimate juiciness)",
                "Kosher salt and black pepper added only right after smashing",
                "Mayo, ketchup, and pickle relish quick burger sauce",
                "Soft potato bun or standard sesame hamburger bun"
            ),
            ingredients = listOf(
                IngredientItem("Ground beef (80/20)", 100.0, "g", "Protein", true, "Ground chicken, turkey, or black bean burger"),
                IngredientItem("American cheese or cheddar slice", 1.0, "pcs", "Dairy", true, null),
                IngredientItem("Burger bun", 1.0, "pcs", "Pantry", true, "Regular sliced bread toasted in butter"),
                IngredientItem("Yellow onion, thinly shaved", 0.25, "pcs", "Produce", false, null),
                IngredientItem("Mayonnaise", 1.0, "tbsp", "Pantry", true, null),
                IngredientItem("Ketchup", 1.0, "tsp", "Pantry", true, null),
                IngredientItem("Butter for toasting", 1.0, "tsp", "Dairy", true, null),
                IngredientItem("Salt & coarse black pepper", 0.5, "tsp", "Spices", true, null)
            ),
            instructions = listOf(
                CookingStep(
                    stepNumber = 1,
                    title = "Toast the Buns",
                    instruction = "Spread butter on the cut sides of the bun. Place face down in a dry skillet over medium heat for 1-2 minutes until golden brown and toasted. Remove and set aside.",
                    timerSeconds = 90,
                    proTip = "Toasted buns prevent burger sauce from making the bottom bun soggy."
                ),
                CookingStep(
                    stepNumber = 2,
                    title = "Form the Beef Ball",
                    instruction = "Lightly shape the ground beef into a loose, round ball. DO NOT pack it tight or press it yet.",
                    timerSeconds = 30,
                    proTip = "Packing beef tight makes tough burgers; loose balls smash flatter."
                ),
                CookingStep(
                    stepNumber = 3,
                    title = "The Hard Smash",
                    instruction = "Get your skillet screaming hot over high heat until slightly smoking. Place the meat ball in the center. Top with sliced onions. Place parchment paper over the meat and SMASH down hard with a heavy spatula until paper thin with jagged edges.",
                    timerSeconds = 45,
                    proTip = "The harder you press initially, the crispier the caramelized lace edges will be."
                ),
                CookingStep(
                    stepNumber = 4,
                    title = "Sear, Season & Flip",
                    instruction = "Immediately season the smashed meat generously with salt and pepper. Cook undisturbed for 2 minutes until edges are dark and crispy. Scrape underneath hard and flip over.",
                    timerSeconds = 120,
                    proTip = "Scrape right against the metal pan to keep that precious dark crust attached."
                ),
                CookingStep(
                    stepNumber = 5,
                    title = "Melt Cheese & Assemble",
                    instruction = "Immediately slap the cheese slice over the hot flipped patty. Cover pan for 30 seconds to melt cheese. Mix mayo and ketchup for burger sauce, spread on toasted buns, slide patty onto bun, and devour!",
                    timerSeconds = 45,
                    proTip = "Steam melts cheese in seconds."
                )
            ),
            equipment = listOf("Heavy skillet (cast iron or stainless steel)", "Stiff metal spatula", "Parchment paper square"),
            allergyWarnings = listOf("Contains Dairy (cheese, butter) and Gluten (bun)."),
            foodSafetyNote = "Ground beef safe minimum internal cooking temp: 71°C (160°F). Thin smashed patties cook through completely in under 3 minutes.",
            studentBudgetTips = listOf(
                "Buy ground beef in family packs on sale and freeze individual 100g balls in cling wrap",
                "Costs $1.65 / ৳195 at home vs $8-$12 at cafes and takeout spots",
                "One skillet cleanup means zero hassle for bachelors"
            ),
            youtubeQuery = "how to make best smash burger at home beginner",
            isFavorite = false
        )
    )
}
