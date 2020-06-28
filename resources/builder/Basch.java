package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Basch extends _Default{
	public Basch() {
		super("Basch", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5681};
				break;
			case HP:
				ids = new int[]{5682};
				break;
			case S1:
				ids = new int[]{5265};
				break;
			case S2:
				ids = new int[]{5270};
				break;
			case EX:
				ids = new int[]{5690, 5689};
				break;
			case AA:
				ids = new int[]{5694};
				break;
			case LD:
				ids = new int[]{9204};
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