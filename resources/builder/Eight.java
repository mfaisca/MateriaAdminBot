package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Eight extends _Default{
	public Eight() {
		super("Eight", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6289};
				break;
			case HP:
				ids = new int[]{6597, 6598, 6292};
				break;
			case S1:
				ids = new int[]{5939};
				break;
			case S2:
				ids = new int[]{5945};
				break;
			case EX:
				ids = new int[]{6300};
				break;
			case AA:
				ids = new int[]{6306};
				break;
			case LD:
				ids = new int[]{10074};
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