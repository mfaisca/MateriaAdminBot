package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Deuce extends _Default{
	public Deuce() {
		super("Deuce", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5226};
				break;
			case HP:
				ids = new int[]{5214};
				break;
			case S1:
				ids = new int[]{5209};
				break;
			case S2:
				ids = new int[]{5222};
				break;
			case EX:
				ids = new int[]{5230};
				break;
			case AA:
				ids = new int[]{5234};
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