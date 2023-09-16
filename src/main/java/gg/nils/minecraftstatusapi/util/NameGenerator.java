package gg.nils.minecraftstatusapi.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NameGenerator {

    private static final List<String> ADJECTIVES = Arrays.asList(
            "amazing",
            "awesome",
            "big",
            "brave",
            "bright",
            "clean",
            "cold",
            "cool",
            "creative",
            "crying",
            "dry",
            "elegant",
            "excellent",
            "excited",
            "fabulous",
            "fancy",
            "friendly",
            "funny",
            "good",
            "gorgeous",
            "great",
            "happy",
            "heavy",
            "hot",
            "kind",
            "long",
            "loyal",
            "perfect",
            "polite",
            "quiet",
            "short",
            "small",
            "sour",
            "sweet",
            "unique",
            "wet"
    );

    private static final List<String> ITEMS = Arrays.asList(
            "apple",
            "beacon",
            "bedrock",
            "cake",
            "coal",
            "diamond",
            "dirt",
            "dropper",
            "egg",
            "emerald",
            "gold",
            "hopper",
            "ice",
            "iron",
            "ladder",
            "lever",
            "map",
            "minecart",
            "observer",
            "obsidian",
            "piston",
            "potion",
            "pumpkin",
            "quartz",
            "redstone",
            "saddle",
            "shears",
            "slime",
            "snow",
            "sponge",
            "stick",
            "stone",
            "string",
            "sugar",
            "tnt",
            "torch",
            "water",
            "wheat"
    );

    public static String generate() {
        ThreadLocalRandom current = ThreadLocalRandom.current();

        return "%s-%s".formatted(
                ADJECTIVES.get(current.nextInt(ADJECTIVES.size())),
                ITEMS.get(current.nextInt(ITEMS.size()))
        );
    }
}
