package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Edward extends _Default{
	public Edward() {
		super("Edward", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed", "ed");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9697};
				break;
			case HP:
				ids = new int[]{9701};
				break;
			case S1:
				ids = new int[]{9684};
				break;
			case S2:
				ids = new int[]{9396, 9392};
				break;
			case EX:
				ids = new int[]{9688};
				break;
			case AA:
				ids = new int[]{9713};
				break;
			case LD:
				ids = new int[]{9693};
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