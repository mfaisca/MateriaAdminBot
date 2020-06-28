package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tifa extends _Default{
	public Tifa() {
		super("Tifa", "lockhart");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6003};
				break;
			case HP:
				ids = new int[]{6004};
				break;
			case S1:
				ids = new int[]{5786, 6002};
				break;
			case S2:
				ids = new int[]{5788};
				break;
			case EX:
				ids = new int[]{5792};
				break;
			case AA:
				ids = new int[]{5797};
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