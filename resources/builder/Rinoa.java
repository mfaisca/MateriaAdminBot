package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Rinoa extends _Default{
	public Rinoa() {
		super("Rinoa", "heartilly", "heartilly");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9499};
				break;
			case HP:
				ids = new int[]{3652};
				break;
			case S1:
				ids = new int[]{3631};
				break;
			case S2:
				ids = new int[]{3637};
				break;
			case EX:
				ids = new int[]{6521, 6523};
				break;
			case AA:
				ids = new int[]{6763};
				break;
			case LD:
				ids = new int[]{9565};
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