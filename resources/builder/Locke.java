package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Locke extends _Default{
	public Locke() {
		super("Locke", "lock", "lock");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{4710};
				break;
			case HP:
				ids = new int[]{5784};
				break;
			case S1:
				ids = new int[]{4708, 4709};
				break;
			case S2:
				ids = new int[]{4714};
				break;
			case EX:
				ids = new int[]{5648};
				break;
			case AA:
				ids = new int[]{4705};
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