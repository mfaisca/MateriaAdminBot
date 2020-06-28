package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Jack extends _Default{
	public Jack() {
		super("Jack", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{7147};
				break;
			case HP:
				ids = new int[]{7148};
				break;
			case S1:
				ids = new int[]{7133};
				break;
			case S2:
				ids = new int[]{7142};
				break;
			case EX:
				ids = new int[]{7924};
				break;
			case AA:
				ids = new int[]{7928};
				break;
			case LD:
				ids = new int[]{11014};
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