package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Terra extends _Default{
	public Terra() {
		super("Terra", "tina", "tina");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6766};
				break;
			case HP:
				ids = new int[]{6767};
				break;
			case S1:
				ids = new int[]{4444, 4416};
				break;
			case S2:
				ids = new int[]{4423, 4446};
				break;
			case EX:
				ids = new int[]{6575};
				break;
			case AA:
				ids = new int[]{4359};
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