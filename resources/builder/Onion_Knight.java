package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Onion_Knight extends _Default{
	public Onion_Knight() {
		super("Onion Knight", "ok", "ok", "ok", "ok");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{2149, 3530};
				break;
			case HP:
				ids = new int[]{6636, 6637};
				break;
			case S1:
				ids = new int[]{3519};
				break;
			case S2:
				ids = new int[]{3527};
				break;
			case EX:
				ids = new int[]{6634, 6635};
				break;
			case AA:
				ids = new int[]{6649};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}