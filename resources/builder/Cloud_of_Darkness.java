package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cloud_of_Darkness extends _Default{
	public Cloud_of_Darkness() {
		super("Cloud of Darkness", "cod", "cod");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8471};
				break;
			case HP:
				ids = new int[]{8474};
				break;
			case S1:
				ids = new int[]{7650};
				break;
			case S2:
				ids = new int[]{7659, 7658};
				break;
			case EX:
				ids = new int[]{8468};
				break;
			case AA:
				ids = new int[]{7664};
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