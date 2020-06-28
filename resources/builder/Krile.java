package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Krile extends _Default{
	public Krile() {
		super("Krile", "kururu", "kururu", "kururu", "kururu");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{11102};
				break;
			case HP:
				ids = new int[]{11326};
				break;
			case S1:
				ids = new int[]{5178, 5177};
				break;
			case S2:
				ids = new int[]{5185, 5184};
				break;
			case EX:
				ids = new int[]{5329};
				break;
			case AA:
				ids = new int[]{5194};
				break;
			case LD:
				ids = new int[]{11107};
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