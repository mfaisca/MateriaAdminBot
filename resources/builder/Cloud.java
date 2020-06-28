package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cloud extends _Default{
	public Cloud() {
		super("Cloud", "strife", "strife", "strife", "strife");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{3591};
				break;
			case HP:
				ids = new int[]{3592};
				break;
			case S1:
				ids = new int[]{3624};
				break;
			case S2:
				ids = new int[]{3590};
				break;
			case EX:
				ids = new int[]{5075};
				break;
			case AA:
				ids = new int[]{5029};
				break;
			case LD:
				ids = new int[]{10012};
				break;
			case BT:
				ids = new int[]{10013};
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