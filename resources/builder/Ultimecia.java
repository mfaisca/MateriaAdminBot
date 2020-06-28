package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ultimecia extends _Default{
	public Ultimecia() {
		super("Ultimecia", "ulti");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5420};
				break;
			case HP:
				ids = new int[]{5326};
				break;
			case S1:
				ids = new int[]{4838, 4837};
				break;
			case S2:
				ids = new int[]{4847, 4848};
				break;
			case EX:
				ids = new int[]{5325};
				break;
			case AA:
				ids = new int[]{4855};
				break;
			case LD:
				ids = new int[]{8586};
				break;
			case BT:
				ids = new int[]{10615};
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