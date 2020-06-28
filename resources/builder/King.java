package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class King extends _Default{
	public King() {
		super("King", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{6319};
				break;
			case HP:
				ids = new int[]{614};
				break;
			case S1:
				ids = new int[]{6316};
				break;
			case S2:
				ids = new int[]{6318};
				break;
			case EX:
				ids = new int[]{6311};
				break;
			case AA:
				ids = new int[]{6323};
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