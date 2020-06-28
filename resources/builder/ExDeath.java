package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExDeath extends _Default{
	public ExDeath() {
		super("ExDeath", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd", "exd");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{7460};
				break;
			case HP:
				ids = new int[]{7461};
				break;
			case S1:
				ids = new int[]{6892};
				break;
			case S2:
				ids = new int[]{6896};
				break;
			case EX:
				ids = new int[]{7459};
				break;
			case AA:
				ids = new int[]{7465};
				break;
			case LD:
				ids = new int[]{10755};
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