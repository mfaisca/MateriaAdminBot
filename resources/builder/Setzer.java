package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Setzer extends _Default{
	public Setzer() {
		super("Setzer", "gabbiani");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6200};
				break;
			case HP:
				ids = new int[]{6099};
				break;
			case S1:
				ids = new int[]{4718};
				break;
			case S2:
				ids = new int[]{2774};
				break;
			case EX:
				ids = new int[]{6201};
				break;
			case AA:
				ids = new int[]{4515};
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