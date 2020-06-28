package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lion extends _Default{
	public Lion() {
		super("Lion", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{4926};
				break;
			case HP:
				ids = new int[]{8443};
				break;
			case S1:
				ids = new int[]{4920, 4921};
				break;
			case S2:
				ids = new int[]{4924, 4925};
				break;
			case EX:
				ids = new int[]{4927, 4928};
				break;
			case AA:
				ids = new int[]{4966};
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