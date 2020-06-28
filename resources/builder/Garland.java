package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Garland extends _Default{
	public Garland() {
		super("Garland", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5116};
				break;
			case HP:
				ids = new int[]{4814};
				break;
			case S1:
				ids = new int[]{4812, 4813};
				break;
			case S2:
				ids = new int[]{4821, 4822};
				break;
			case EX:
				ids = new int[]{5120};
				break;
			case AA:
				ids = new int[]{4830};
				break;
			case LD:
				ids = new int[]{8951, 8950};
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